package dsachat.server;

import java.io.*;
import dsachat.share.*;
import dsachat.share.hero.*;
import java.util.*;
import java.util.regex.*;

public class Handler implements InputListener
{
    private Vector<Entry> clients;
    
    public Handler() {
        this.clients = new Vector<Entry>();
    }
    
    public void addConnection(final ConnectionEndpoint cl) {
        final Entry n = new Entry();
        n.ce = cl;
        n.name = "";
        try {
            while (!cl.isConnected()) {}
            for (int i = 0; i < this.clients.size(); ++i) {
                final UserEvent ev = new UserEvent(false, this.clients.elementAt(i).name);
                cl.send(ev);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.clients.addElement(n);
    }
    
    public void removeConnection(final ConnectionEndpoint cl) {
        String name = null;
        for (int i = 0; i < this.clients.size(); ++i) {
            if (this.clients.elementAt(i).ce.getClientPort() == cl.getClientPort()) {
                name = this.clients.elementAt(i).name;
                this.clients.removeElementAt(i);
                break;
            }
        }
        if (name != null) {
            final UserEvent newCon = new UserEvent(true, name);
            this.sendAllObj(newCon);
        }
    }
    
    @Override
    public void handleInput(final InputEvent e) {
        final Object o = e.getData();
        final ConnectionEndpoint con = e.getSource();
        if (o.getClass().equals(String.class)) {
            String msg = o.toString();
            if (msg.startsWith("/")) {
                final String[] cmd = msg.substring(1).split("\\s", 2);
                msg = cmd[1];
                if (cmd[0].matches("(d(ice)?)|(w\u00fcrfel)")) {
                    this.rollDice(con, msg.trim());
                    return;
                }
                if (cmd[0].matches("(w(hisper)?)|(f(l\u00fcster)?)")) {
                    this.whisper(con, msg.trim());
                    return;
                }
                if (cmd[0].matches("me")) {
                    this.sendAllObj(new String("*" + this.getName(con) + ": " + msg));
                    return;
                }
                if (cmd[0].equals("/getheros")) {
                    this.sendHeros(con);
                    return;
                }
                this.sendOneSystemMsg(con, "command /" + cmd[0] + " not found");
                return;
            }
            else {
                this.sendAllStr(con, o.toString());
            }
        }
        if (o.getClass().equals(Challenge.class)) {
            final Challenge c = (Challenge)o;
            final String send = c.roll();
            final String to = c.getTo();
            if (to == null) {
                this.sendAllStr(con, send);
            }
            else {
                if (!c.isSilent()) {
                    try {
                        con.send(send);
                    }
                    catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                this.sendToOneStr(con, to, send);
            }
        }
        if (o.getClass().equals(ChallReq.class)) {
            final ChallReq cr = (ChallReq)o;
            for (final Entry i : this.clients) {
                if (i.name.equals(cr.getTo())) {
                    try {
                        i.ce.send(cr);
                    }
                    catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    return;
                }
            }
        }
        if (o.getClass().equals(Hero.class)) {
            final Hero h = (Hero)o;
            final String name = this.updateHero(con, h);
            if (name == null) {
                System.out.println(this.getName(con) + " updated his hero");
                return;
            }
            final UserEvent newCon = new UserEvent(false, name);
            System.out.println(this.getName(con) + " connected");
            this.sendAllObj(newCon);
            try {
                con.send("Welcome " + h.getName());
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (o.getClass().equals(UserEvent.class) && !this.isGmConnected()) {
            final UserEvent newCon2 = (UserEvent)o;
            for (int j = 0; j < this.clients.size(); ++j) {
                if (this.clients.elementAt(j).ce.getClientPort() == con.getClientPort()) {
                    this.clients.elementAt(j).name = "Gamemaster";
                }
            }
            System.out.println("Gamemaster connected");
            this.sendAllObj(newCon2);
            try {
                con.send("Welcome " + newCon2.getName());
            }
            catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }
    
    private void sendHeros(final ConnectionEndpoint con) {
        final Vector<Hero> vh = new Vector<Hero>();
        for (final Entry e : this.clients) {
            if (e.hero != null) {
                vh.addElement(e.hero);
            }
        }
        try {
            con.send(vh);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    private boolean isGmConnected() {
        for (int i = 0; i < this.clients.size(); ++i) {
            if (this.clients.elementAt(i).name.equals("Gamemaster")) {
                return true;
            }
        }
        return false;
    }
    
    private void sendOneSystemMsg(final ConnectionEndpoint con, final String string) {
        try {
            con.send(string);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void sendAllSystemMsg(final String string) {
        for (int i = 0; i < this.clients.size(); ++i) {
            try {
                this.clients.elementAt(i).ce.send(string);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void whisper(final ConnectionEndpoint con, final String cmd) {
        final String[] split = cmd.split("\\s", 2);
        final String to = split[0];
        final String tail = split[1];
        String send = this.getName(con) + " whispers: " + tail;
        for (int i = 0; i < this.clients.size(); ++i) {
            if (this.clients.elementAt(i).name.equals(to)) {
                this.sendOneSystemMsg(this.clients.elementAt(i).ce, send);
            }
        }
        send = "You whisper to " + to + ": " + tail;
        this.sendOneSystemMsg(con, send);
    }
    
    private void rollDice(final ConnectionEndpoint con, final String cmd) {
        if (cmd.matches(".*\\+.*")) {
            final Matcher m = Pattern.compile("(\\d*)[wd](\\d+)\\+(\\d+).*").matcher(cmd);
            if (m.matches()) {
                final int count = (m.group(1) == null || m.group(1).equals("")) ? 1 : Integer.parseInt(m.group(1));
                final int sides = Integer.parseInt(m.group(2));
                final int mod = Integer.parseInt(m.group(3));
                String ret = this.getName(con) + " rolled " + cmd + " --> ";
                int sum = 0;
                for (int i = 0; i < count; ++i) {
                    sum += Challenge.dice(sides);
                }
                sum += mod;
                ret += sum;
                this.sendAllSystemMsg(ret);
            }
        }
        else {
            final Matcher m = Pattern.compile("(\\d*)[wd](\\d+).*").matcher(cmd);
            if (m.matches()) {
                final int count = (m.group(1) == null || m.group(1).equals("")) ? 1 : Integer.parseInt(m.group(1));
                final int sides = Integer.parseInt(m.group(2));
                String ret2 = this.getName(con) + " rolls " + cmd + " --> ";
                for (int j = 0; j < count; ++j) {
                    ret2 = ret2 + Challenge.dice(sides) + "  ";
                }
                this.sendAllSystemMsg(ret2);
            }
        }
    }
    
    private void sendToOneStr(final ConnectionEndpoint con, final String to, final String send) {
        final String sender = this.getName(con);
        for (int i = 0; i < this.clients.size(); ++i) {
            if (this.clients.elementAt(i).name.equals(to)) {
                try {
                    final String msg = sender + ": " + send;
                    this.clients.elementAt(i).ce.send(msg);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private String updateHero(final ConnectionEndpoint con, final Hero h) {
        int i = 0;
        while (i < this.clients.size()) {
            if (this.clients.elementAt(i).ce.getClientPort() == con.getClientPort()) {
                this.clients.elementAt(i).hero = h;
                if (this.clients.elementAt(i).name.equals("")) {
                    this.clients.elementAt(i).name = h.getName();
                    return h.getName();
                }
                return null;
            }
            else {
                ++i;
            }
        }
        return null;
    }
    
    private void sendAllStr(final ConnectionEndpoint con, final String string) {
        final String sender = this.getName(con);
        for (int i = 0; i < this.clients.size(); ++i) {
            try {
                final String msg = sender + ": " + string;
                this.clients.elementAt(i).ce.send(msg);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void sendAllObj(final Object o) {
        for (int i = 0; i < this.clients.size(); ++i) {
            try {
                this.clients.elementAt(i).ce.send(o);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private String getName(final ConnectionEndpoint con) {
        for (int i = 0; i < this.clients.size(); ++i) {
            if (this.clients.elementAt(i).ce.getClientPort() == con.getClientPort()) {
                return this.clients.elementAt(i).name;
            }
        }
        return null;
    }
}
