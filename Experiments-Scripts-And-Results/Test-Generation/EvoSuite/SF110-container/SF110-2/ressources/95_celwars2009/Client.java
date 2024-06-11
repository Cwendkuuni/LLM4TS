import java.lang.reflect.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.net.*;
import java.awt.image.*;
import java.awt.*;

public class Client extends JFrame
{
    private JField Field;
    private JMiniMap minimap;
    private JRadioButton FoodBtn;
    private JRadioButton FlagBtn;
    private JLabel Power;
    private JLabel Score;
    private JLabel Time;
    private JList doList;
    private JTextPane description;
    private ArrayList<drawCircle> circles;
    private boolean connected;
    private ArrayList<Message> messages;
    private int top;
    private int left;
    private int worldSize;
    private ServerThread S;
    private Random gen;
    private Sound sfx;
    private MP3 currentMP3;
    private OptionList theList;
    private Timer ambience;
    private ArrayList<Chain> chains;
    private ArrayList<ArrayList<ScorePoint>> elevation;
    private ArrayList<ArrayList<ScorePoint>> scores;
    private ArrayList<CatmullRomSpline> blobs;
    private int maxScore;
    private int time;
    private boolean infoing;
    private boolean chart;
    private Client self;
    
    private void UpdateChains() {
        for (int i = 0; i < this.chains.size(); ++i) {
            for (int j = 0; j < this.chains.get(i).points.length; ++j) {
                final double[] array = this.chains.get(i).points[j];
                final int n = 0;
                array[n] += this.gen.nextGaussian();
                final double[] array2 = this.chains.get(i).points[j];
                final int n2 = 1;
                array2[n2] += this.gen.nextGaussian();
            }
            for (int j = 0; j < this.chains.get(i).points.length - 1; ++j) {
                final double dx = this.chains.get(i).points[j + 1][0] - this.chains.get(i).points[j][0];
                final double dy = this.chains.get(i).points[j + 1][1] - this.chains.get(i).points[j][1];
                final double mag = Math.sqrt(dx * dx + dy * dy);
                this.chains.get(i).points[j + 1][0] = this.chains.get(i).points[j][0] + dx / mag * 5.0;
                this.chains.get(i).points[j + 1][1] = this.chains.get(i).points[j][1] + dy / mag * 5.0;
            }
        }
        for (int i = 0; i < this.blobs.size(); ++i) {
            for (int j = 3; j < this.blobs.get(i).controlPoints_.length - 6; ++j) {
                if (this.blobs.get(i).controlPoints_[j] != 0.0) {
                    final double[] controlPoints_ = this.blobs.get(i).controlPoints_;
                    final int n3 = j;
                    controlPoints_[n3] += this.gen.nextGaussian() * 0.3;
                }
            }
            this.blobs.get(i).controlPoints_[this.blobs.get(i).controlPoints_.length - 6] = this.blobs.get(i).controlPoints_[3];
            this.blobs.get(i).controlPoints_[this.blobs.get(i).controlPoints_.length - 5] = this.blobs.get(i).controlPoints_[4];
            this.blobs.get(i).controlPoints_[this.blobs.get(i).controlPoints_.length - 4] = this.blobs.get(i).controlPoints_[5];
        }
    }
    
    public void connect(final String string, final int i) {
        try {
            if (this.S == null) {
                (this.S = new ServerThread(new Socket(string, i), this)).start();
            }
            else {
                this.sendMessage("quit");
                Thread.sleep(1000L);
                this.circles.clear();
                (this.S = new ServerThread(new Socket(string, i), this)).start();
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this.self, "Having trouble connecting to the server...");
        }
    }
    
    private Color getColor(final String trying) {
        Color c = Color.BLACK;
        try {
            final Class ColClas = Class.forName("java.awt.Color");
            final Field f = ColClas.getField(trying);
            c = (Color)f.get(null);
        }
        catch (Exception ex) {}
        return c;
    }
    
    public Client() {
        this.Field = new JField();
        this.minimap = new JMiniMap();
        this.circles = new ArrayList<drawCircle>();
        this.connected = false;
        this.messages = new ArrayList<Message>();
        this.top = 0;
        this.left = 0;
        this.worldSize = 2000;
        this.S = null;
        this.gen = new Random();
        this.sfx = null;
        this.currentMP3 = null;
        this.theList = null;
        this.ambience = new Timer(100000, new ambienceTimer());
        this.chains = new ArrayList<Chain>();
        this.elevation = new ArrayList<ArrayList<ScorePoint>>();
        this.scores = new ArrayList<ArrayList<ScorePoint>>();
        this.blobs = new ArrayList<CatmullRomSpline>();
        this.maxScore = 30;
        this.time = 0;
        this.infoing = true;
        this.chart = false;
        (this.self = this).initialize();
        (this.sfx = new Sound()).initialize();
    }
    
    private void initialize() {
        final JMenuBar menu = new JMenuBar();
        menu.setMaximumSize(new Dimension(80, 15));
        menu.setPreferredSize(new Dimension(80, 15));
        menu.setFont(new Font("Halvetica", 0, 10));
        final JMenu menuFile = new JMenu("File");
        menu.add(menuFile);
        final JMenuItem menuConnect = new JMenuItem("Connect...");
        menuConnect.setPreferredSize(new Dimension(60, 15));
        menuConnect.setFont(new Font("Halvetica", 0, 10));
        menuConnect.addActionListener(new connectListener());
        menuFile.add(menuConnect);
        final JMenuItem menuIntro = new JMenuItem("Intro...");
        menuIntro.setPreferredSize(new Dimension(60, 15));
        menuIntro.setFont(new Font("Halvetica", 0, 10));
        menuIntro.addActionListener(new introListener());
        menuFile.add(menuIntro);
        this.setJMenuBar(menu);
        this.addWindowListener(new windowCloseListener());
        final Container cp = this.getContentPane();
        cp.setLayout(new BorderLayout());
        this.Field.setMaximumSize(new Dimension(700, 700));
        this.Field.setPreferredSize(new Dimension(700, 700));
        this.Field.addMouseListener(new fieldClicker());
        cp.add(this.Field, "Center");
        final JPanel right = new JPanel();
        right.setLayout(new FlowLayout(5, 5, 1));
        right.setMaximumSize(new Dimension(200, 700));
        right.setPreferredSize(new Dimension(200, 700));
        this.minimap.setPreferredSize(new Dimension(200, 200));
        this.minimap.setMaximumSize(new Dimension(200, 200));
        this.minimap.addMouseListener(new miniClickListener());
        right.add(this.minimap);
        final JPanel adders = new JPanel();
        adders.setLayout(new BorderLayout());
        this.Power = new JLabel("Evolution 0");
        this.Time = new JLabel("Time 0");
        this.Score = new JLabel("Score 0");
        final JPanel info = new JPanel();
        info.setLayout(new GridLayout(4, 1));
        info.add(this.Power);
        info.add(this.Score);
        info.add(this.Time);
        info.add(new JLabel("Possible Actions and Cost:"));
        adders.add(info, "North");
        this.theList = new OptionList();
        this.theList.items.add("Place Food 100");
        this.theList.items.add("Place Pheremone 20");
        this.theList.items.add("Place Inorganic 50");
        this.theList.items.add("Food Receptors, 40");
        this.theList.items.add("Simple Flagella, 70");
        this.theList.items.add("Senescence Seggregation, 40");
        this.theList.items.add("Toxic Apoptosis, 200");
        this.theList.items.add("Antibiotic Receptors, 50");
        this.theList.items.add("Group Growth, 250");
        this.theList.items.add("Aggression, 78");
        this.theList.items.add("Cellular Walls, 290");
        this.theList.items.add("Lysozymes, 310");
        this.theList.items.add("Antibiotic Resistance, 200");
        this.theList.items.add("Telomerase, 500");
        this.theList.items.add("Group Attraction, 150");
        this.theList.items.add("Complex Flagella, 150");
        this.theList.items.add("Photosynthesis, 700");
        this.theList.items.add("Triplication, 650");
        this.theList.items.add("Release Antibiotic, 3000");
        this.theList.items.add("Enzymatic Revolution, 5000");
        (this.doList = new JList(this.theList)).setFont(new Font("Helvetica", 0, 10));
        final JScrollPane sb = new JScrollPane(this.doList);
        this.doList.addListSelectionListener(new doListListener());
        sb.setPreferredSize(new Dimension(190, 300));
        sb.setMaximumSize(new Dimension(190, 300));
        right.add(adders);
        right.add(sb);
        (this.description = new JTextPane()).setText("Click an action above for details...");
        this.description.setEditable(false);
        this.description.setPreferredSize(new Dimension(190, 120));
        this.description.setMaximumSize(new Dimension(190, 120));
        this.description.setFont(new Font("Helvetica", 0, 10));
        right.add(this.description);
        right.invalidate();
        cp.add(right, "East");
        this.validate();
        this.setDefaultCloseOperation(3);
        this.setSize(920, 735);
        this.setResizable(false);
        this.setTitle("CellWars by Max Shokhirev 2008");
        this.setVisible(true);
    }
    
    private void inializeElevation() {
        this.elevation.add(new ArrayList<ScorePoint>());
        this.elevation.add(new ArrayList<ScorePoint>());
        this.elevation.add(new ArrayList<ScorePoint>());
        this.elevation.add(new ArrayList<ScorePoint>());
        this.elevation.add(new ArrayList<ScorePoint>());
        for (int i = 0; i < this.worldSize / 100; ++i) {
            final int e = this.gen.nextInt(5);
            final int x = this.gen.nextInt(this.worldSize);
            final int y = this.gen.nextInt(this.worldSize);
            for (int j = 4 - e; j <= 4; ++j) {
                this.elevation.get(j).add(new ScorePoint(x, y, 10 * j + 10));
            }
        }
    }
    
    private void initializeBlobs() {
        for (int many = 1 + this.gen.nextInt(3), i = 0; i < many; ++i) {
            int x = this.gen.nextInt(this.worldSize);
            int y = this.gen.nextInt(this.worldSize);
            int dx = 0;
            int dy = 0;
            final int ccount = 6;
            final double[] c = new double[3 * ccount + 3];
            for (int j = 0; j < ccount; ++j) {
                c[j * 3] = x;
                c[j * 3 + 1] = y;
                c[j * 3 + 2] = 0.0;
                if (j < 3) {
                    dx += (int)(Math.cos(j * 2) * 40.0 + this.gen.nextInt(5) - this.gen.nextInt(5));
                    dy += (int)(Math.sin(j * 2) * 40.0 + this.gen.nextInt(5) - this.gen.nextInt(5));
                }
                else {
                    dx -= (int)(Math.cos(j * 2) * 40.0 + this.gen.nextInt(5) - this.gen.nextInt(5));
                    dy -= (int)(Math.sin(j * 2) * 40.0 + this.gen.nextInt(5) - this.gen.nextInt(5));
                }
                x += dx;
                y += dy;
            }
            c[3 * ccount] = c[0];
            c[3 * ccount + 1] = c[1];
            c[3 * ccount + 2] = c[2];
            this.blobs.add(new CatmullRomSpline(c, ccount * 4));
        }
    }
    
    private void initializeChains() {
        for (int numChains = 5 + this.gen.nextInt(5), i = 0; i < numChains; ++i) {
            final Chain c = new Chain(10 + this.gen.nextInt(15));
            c.points[0][0] = this.gen.nextDouble() * this.worldSize;
            c.points[0][1] = this.gen.nextDouble() * this.worldSize;
            for (int j = 1; j < c.length; ++j) {
                c.points[j][0] = c.points[j - 1][0] + this.gen.nextGaussian() * 5.0;
                c.points[j][1] = c.points[j - 1][1] + this.gen.nextGaussian() * 5.0;
            }
            this.chains.add(c);
        }
    }
    
    public void sendMessage(final String m) {
        if (this.S != null) {
            this.S.sendMessage(m);
        }
    }
    
    private void playMP3(final String name) {
        this.currentMP3 = new MP3(name);
    }
    
    public Image loadTransImage(final String path) {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final Image newImage = tk.getImage(path);
        final MediaTracker mediaTracker = new MediaTracker(this);
        mediaTracker.addImage(newImage, 0);
        try {
            mediaTracker.waitForID(0);
        }
        catch (InterruptedException ie) {
            System.err.println(ie);
            System.exit(1);
        }
        return newImage;
    }
    
    public void analyzeServerMessage(final String incomming) {
        if (incomming.startsWith("Sound ") && incomming.length() > 6) {
            final StringTokenizer tokener = new StringTokenizer(incomming.substring(6, incomming.length()), ";");
            while (tokener.hasMoreTokens()) {
                final Scanner S = new Scanner(tokener.nextToken());
                final String sound = S.next();
                final int x = S.nextInt();
                final int y = S.nextInt();
                if (x == -1 && y == -1) {
                    this.sfx.playSound(sound);
                }
                else {
                    if (x < this.left || x > this.left + 700 || y < this.top || y > this.top + 700) {
                        continue;
                    }
                    this.sfx.playSound(sound);
                    System.err.println("Playing sound..." + sound + " x " + x + " y " + y);
                }
            }
        }
        else if (incomming.startsWith("MP3 ")) {
            this.playMP3(incomming.substring(4, incomming.length()));
        }
        else if (incomming.startsWith("Message ")) {
            final Scanner S2 = new Scanner(incomming.substring(8, incomming.length()));
            this.messages.add(new Message(S2.nextInt(), S2.nextLine()));
            this.Field.updateUI();
        }
        else if (incomming.startsWith("Message2")) {
            final Scanner S2 = new Scanner(incomming.substring(9, incomming.length()));
            this.messages.add(new Message(S2.nextInt(), S2.nextLine(), true));
            this.chart = true;
            this.Field.updateUI();
        }
        else if (incomming.startsWith("Size")) {
            this.worldSize = Integer.parseInt(incomming.substring(4, incomming.length()));
            this.initializeChains();
            this.inializeElevation();
            this.initializeBlobs();
        }
        else if (incomming.startsWith("Center")) {
            final Scanner S2 = new Scanner(incomming.substring(6, incomming.length()));
            if (this.infoing) {
                this.left = S2.nextInt() - 350;
                this.top = S2.nextInt() - 350;
            }
        }
        else if (incomming.startsWith("Update")) {
            final StringTokenizer tokener = new StringTokenizer(incomming.substring(7, incomming.length()), ";");
            int index = 0;
            while (tokener.hasMoreTokens()) {
                final Scanner S3 = new Scanner(tokener.nextToken());
                final int color = Integer.parseInt(S3.next());
                this.time = Integer.parseInt(S3.next());
                final int theScore = Integer.parseInt(S3.next());
                if (theScore > this.maxScore) {
                    this.maxScore = theScore;
                }
                if (this.scores.size() <= index) {
                    final ArrayList<ScorePoint> theList = new ArrayList<ScorePoint>();
                    this.scores.add(theList);
                }
                this.scores.get(index).add(new ScorePoint(this.time, theScore, color));
                if (index == 0) {
                    this.Score.setText("Score " + theScore);
                    this.Time.setText("Time " + this.time);
                    if (theScore < 100) {
                        this.Score.setForeground(Color.RED);
                    }
                    else if (theScore >= 100 && theScore < 200) {
                        this.Score.setForeground(Color.ORANGE);
                    }
                    else if (theScore >= 200 && theScore < 400) {
                        this.Score.setForeground(Color.BLACK);
                    }
                    else if (theScore >= 400 && theScore < 600) {
                        this.Score.setForeground(Color.GREEN);
                    }
                    else {
                        this.Score.setForeground(Color.GREEN.brighter());
                    }
                    final double Evo = Double.parseDouble(S3.next());
                    if (Evo < 150.0) {
                        this.Power.setForeground(Color.RED);
                    }
                    else if (Evo >= 150.0 && Evo < 500.0) {
                        this.Power.setForeground(Color.ORANGE);
                    }
                    else if (Evo >= 500.0 && Evo < 1000.0) {
                        this.Power.setForeground(Color.BLACK);
                    }
                    else if (Evo >= 1000.0 && Evo < 2000.0) {
                        this.Power.setForeground(Color.GREEN);
                    }
                    else {
                        this.Power.setForeground(Color.GREEN.brighter());
                    }
                    this.Power.setText("Evolution " + (int)Evo);
                    this.Time.updateUI();
                    this.Score.updateUI();
                    this.Power.updateUI();
                    if (S3.hasNext() && this.infoing) {
                        String d = S3.next();
                        while (S3.hasNext()) {
                            d = d + " " + S3.next();
                        }
                        try {
                            this.description.setText(d);
                            this.description.updateUI();
                        }
                        catch (NullPointerException ex) {}
                    }
                }
                ++index;
            }
        }
        else if (incomming.startsWith("Circles")) {
            final StringTokenizer tokener = new StringTokenizer(incomming.substring(8, incomming.length()), ";");
            while (tokener.hasMoreTokens()) {
                final Scanner S = new Scanner(tokener.nextToken());
                final int id = S.nextInt();
                final double x2 = S.nextDouble();
                final double y2 = S.nextDouble();
                final double size = S.nextDouble();
                final int color2 = S.nextInt();
                if (size >= 8.0) {
                    this.addCircle(new drawCircle(x2, y2, size, color2, id));
                }
                else {
                    this.removeWithId(id);
                }
            }
            this.UpdateChains();
            this.minimap.updateUI();
            this.Field.updateUI();
        }
    }
    
    private void removeWithId(final int id) {
        for (int i = 0; i < this.circles.size(); ++i) {
            if (this.circles.get(i).id == id) {
                this.circles.remove(i);
                return;
            }
        }
    }
    
    private void addCircle(final drawCircle d) {
        boolean doAdd = true;
        for (int i = 0; i < this.circles.size(); ++i) {
            if (this.circles.get(i).id == d.id) {
                doAdd = false;
                this.circles.get(i).c = d.c;
                this.circles.get(i).size = d.size;
                this.circles.get(i).x = d.x;
                this.circles.get(i).y = d.y;
                break;
            }
        }
        if (doAdd) {
            this.circles.add(d);
        }
    }
    
    public static void main(final String[] args) {
        new Client();
    }
    
    public boolean isConnected() {
        return this.connected;
    }
    
    public void setConnected(final boolean connected) {
        this.connected = connected;
    }
    
    public JField getField() {
        return this.Field;
    }
    
    private class ScorePoint
    {
        int x;
        int y;
        int color;
        
        public ScorePoint(final int x, final int y, final int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }
    
    private class Chain
    {
        double[][] points;
        int length;
        
        public Chain(final int length) {
            this.points = new double[30][2];
            this.length = 0;
            this.length = length;
        }
    }
    
    private class ambienceTimer implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent arg0) {
            Client.this.playMP3("ambient4.mp3");
        }
    }
    
    private class Message
    {
        String message;
        Color c;
        long time;
        boolean permanent;
        
        public Message(final int col, final String m) {
            this.permanent = false;
            this.message = m;
            this.c = new Color(col);
            this.time = System.currentTimeMillis();
        }
        
        public Message(final int col, final String m, final boolean b) {
            this.permanent = false;
            this.message = m;
            this.c = new Color(col);
            this.time = System.currentTimeMillis();
            this.permanent = true;
        }
    }
    
    private class JMiniMap extends JPanel
    {
        public void paintComponent(final Graphics g) {
            super.paintComponent(g);
            final Graphics2D g2 = (Graphics2D)g;
            g2.setColor(new Color(200, 230, 230));
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
            for (int i = 0; i < Client.this.circles.size(); ++i) {
                final drawCircle dc = Client.this.circles.get(i);
                g2.setColor(dc.c);
                final int x = (int)(dc.getX() * 200.0 / Client.this.worldSize);
                final int y = (int)(dc.getY() * 200.0 / Client.this.worldSize);
                int size = (int)(dc.size * 200.0 / Client.this.worldSize);
                if (size < 1) {
                    size = 1;
                }
                g2.fillOval(x - size / 2, y - size / 2, size, size);
            }
            g2.setColor(Color.WHITE);
            g2.drawRect(200 * Client.this.left / Client.this.worldSize, 200 * Client.this.top / Client.this.worldSize, 140000 / Client.this.worldSize, 140000 / Client.this.worldSize);
        }
    }
    
    private class windowCloseListener implements WindowListener
    {
        @Override
        public void windowOpened(final WindowEvent arg0) {
        }
        
        @Override
        public void windowClosing(final WindowEvent arg0) {
            if (Client.this.connected) {
                System.err.println("Sent quit");
                Client.this.sendMessage("quit");
            }
            Client.this.sfx.playSound("exit.wav");
            try {
                Thread.sleep(800L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void windowClosed(final WindowEvent arg0) {
        }
        
        @Override
        public void windowIconified(final WindowEvent arg0) {
        }
        
        @Override
        public void windowDeiconified(final WindowEvent arg0) {
        }
        
        @Override
        public void windowActivated(final WindowEvent arg0) {
        }
        
        @Override
        public void windowDeactivated(final WindowEvent arg0) {
        }
    }
    
    private class miniClickListener implements MouseListener
    {
        @Override
        public void mouseClicked(final MouseEvent arg0) {
        }
        
        @Override
        public void mousePressed(final MouseEvent arg0) {
            if (arg0.getButton() == 3) {
                Client.this.sendMessage("Center");
            }
            else {
                Client.this.infoing = false;
                Client.this.left = arg0.getX() * Client.this.worldSize / 200 - 350;
                Client.this.top = arg0.getY() * Client.this.worldSize / 200 - 350;
                Client.this.minimap.updateUI();
                Client.this.Field.updateUI();
            }
        }
        
        @Override
        public void mouseReleased(final MouseEvent arg0) {
        }
        
        @Override
        public void mouseEntered(final MouseEvent arg0) {
        }
        
        @Override
        public void mouseExited(final MouseEvent arg0) {
        }
    }
    
    private class fieldClicker implements MouseListener
    {
        @Override
        public void mouseClicked(final MouseEvent arg0) {
        }
        
        @Override
        public void mousePressed(final MouseEvent arg0) {
            if (Client.this.connected) {
                if (arg0.getButton() == 1) {
                    if (Client.this.doList.getSelectedIndex() != -1) {
                        Client.this.sendMessage(Client.this.theList.items.get(Client.this.doList.getSelectedIndex()) + " " + (arg0.getX() + Client.this.left) + " " + (arg0.getY() + Client.this.top));
                    }
                    else {
                        Client.this.sendMessage("info " + (arg0.getX() + Client.this.left) + " " + (arg0.getY() + Client.this.top));
                        Client.this.infoing = true;
                    }
                }
                else if (arg0.getButton() == 3) {
                    Client.this.sendMessage("info " + (arg0.getX() + Client.this.left) + " " + (arg0.getY() + Client.this.top));
                    Client.this.infoing = true;
                }
                else {
                    Client.this.infoing = false;
                }
            }
        }
        
        @Override
        public void mouseReleased(final MouseEvent arg0) {
        }
        
        @Override
        public void mouseEntered(final MouseEvent arg0) {
        }
        
        @Override
        public void mouseExited(final MouseEvent arg0) {
        }
    }
    
    private class drawCircle
    {
        double x;
        double y;
        double size;
        Color c;
        int id;
        
        public drawCircle(final double x, final double y, final double size, final int color, final int id) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.c = new Color(color);
            this.id = id;
        }
        
        public Color getC() {
            return this.c;
        }
        
        public void setC(final Color c) {
            this.c = c;
        }
        
        public double getSize() {
            return this.size;
        }
        
        public void setSize(final double size) {
            this.size = size;
        }
        
        public double getX() {
            return this.x;
        }
        
        public void setX(final double x) {
            this.x = x;
        }
        
        public double getY() {
            return this.y;
        }
        
        public void setY(final double y) {
            this.y = y;
        }
    }
    
    private class introListener implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent arg0) {
            Client.this.sfx.playSound("welcome.wav");
        }
    }
    
    private class connectListener implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent arg0) {
            Client.this.connect(JOptionPane.showInputDialog(null, "IP Address:"), 4000);
        }
    }
    
    private class doListListener implements ListSelectionListener
    {
        @Override
        public void valueChanged(final ListSelectionEvent arg0) {
            Client.this.infoing = false;
            if (Client.this.doList.getSelectedIndex() == 0) {
                Client.this.description.setText("Place a small food particle on the field.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 1) {
                Client.this.description.setText("Place a pheremone molecule that all cells are attracted to.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 2) {
                Client.this.description.setText("Place a clump of innorganic matterial.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 3) {
                Client.this.description.setText("A cell evolves food receptors, attracting it to food if it is mobile.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 4) {
                Client.this.description.setText("A cell is able to swim at medium velocity.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 5) {
                Client.this.description.setText("Dying cells tend to move away from their siblings, causing them to free up resources.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 6) {
                Client.this.description.setText("Cells that die leave behind toxic antibiotics which are harmfull to all cells.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 7) {
                Client.this.description.setText("Antibiotic receptors allow your cell to sense antibiotics and to try to move away.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 8) {
                Client.this.description.setText("Your cells grow faster when near their siblings due to a distribution of labor within the colony.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 9) {
                Client.this.description.setText("Your cells are attracted to rival cells which are smaller in size, but tend to move away from bigger opponents.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 10) {
                Client.this.description.setText("The evolution of cellular walls cause one cell to be more resistant to attacks by rival cells.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 11) {
                Client.this.description.setText("Lysozymes are secreted when attacking rival cells. These enzymes make the opponent more vulnerable to attack.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 12) {
                Client.this.description.setText("Antibiotic resistance reduces the negative effects of being next to antibiotic particles.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 13) {
                Client.this.description.setText("Telomerase more than doubles the cell's longevity.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 14) {
                Client.this.description.setText("Sibling receptors make your cells attracted to its siblings. This causes your cells to move in groups.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 15) {
                Client.this.description.setText("Complex flagella evolution allows your cells to move at an increased rate.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 16) {
                Client.this.description.setText("Photosynthesis allows your cells to slowly grow even in the absence of food\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 17) {
                Client.this.description.setText("Triplication causes your cells to split into three different daughter cells at once!\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 18) {
                Client.this.description.setText("Your cells release antibiotic particles in the direction of nearby rival cells.\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            else if (Client.this.doList.getSelectedIndex() == 19) {
                Client.this.description.setText("Your cells are able to metabolize inorganics as well as antibiotics and rival cells making them grow in almost all environments!\nThe cost is in evolution points\nYou can choose to do this by clicking anywhere on the field.");
            }
            Client.this.description.updateUI();
        }
    }
    
    private class OptionList implements ListModel
    {
        ArrayList<String> items;
        
        private OptionList() {
            this.items = new ArrayList<String>();
        }
        
        @Override
        public int getSize() {
            return this.items.size();
        }
        
        @Override
        public Object getElementAt(final int arg0) {
            return this.items.get(arg0);
        }
        
        @Override
        public void addListDataListener(final ListDataListener arg0) {
        }
        
        @Override
        public void removeListDataListener(final ListDataListener arg0) {
        }
    }
    
    private class ServerThread extends Thread
    {
        Socket theSocket;
        PrintWriter out;
        BufferedReader in;
        Client theClient;
        
        public ServerThread(final Socket theSock, final Client G) {
            this.theSocket = theSock;
            (this.theClient = G).S = this;
            this.theClient.setConnected(true);
            try {
                this.out = new PrintWriter(this.theSocket.getOutputStream());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            try {
                this.in = new BufferedReader(new InputStreamReader(this.theSocket.getInputStream()));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            while (!this.theSocket.isClosed()) {
                String incomming = "";
                try {
                    if (!this.theSocket.isClosed()) {
                        try {
                            incomming = this.in.readLine();
                        }
                        catch (SocketException e2) {
                            System.err.println("Tried to read from a closed stream! Connection terminated.");
                            this.theSocket.close();
                        }
                    }
                    if (incomming != null) {
                        this.theClient.analyzeServerMessage(incomming);
                    }
                    else {
                        this.theClient.analyzeServerMessage("The connection was terminated by the server!");
                        this.theSocket.close();
                        this.theClient.setConnected(false);
                    }
                }
                catch (IOException e) {
                    System.out.println("Client quit abruptly!");
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
        
        public void sendMessage(final String message) {
            this.out.println(message);
            this.out.flush();
        }
        
        public void disconnect() {
            try {
                this.theSocket.close();
            }
            catch (IOException e) {
                System.err.println("Problem disconnecting...");
            }
        }
    }
    
    private class JField extends JPanel
    {
        public void paintComponent(final Graphics g) {
            super.paintComponent(g);
            final Graphics2D g2 = (Graphics2D)g;
            if (Client.this.connected || Client.this.chart) {
                g2.setColor(new Color(200, 230, 230));
                g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                for (int k = Client.this.elevation.size() - 1; k >= 0; --k) {
                    final ArrayList<ScorePoint> level = Client.this.elevation.get(k);
                    Color c2 = Color.BLACK;
                    if (k == 0) {
                        c2 = new Color(175, 205, 205);
                    }
                    else if (k == 1) {
                        c2 = new Color(180, 210, 210);
                    }
                    else if (k == 2) {
                        c2 = new Color(185, 215, 215);
                    }
                    else if (k == 3) {
                        c2 = new Color(190, 220, 220);
                    }
                    else {
                        c2 = new Color(195, 225, 225);
                    }
                    g2.setColor(c2);
                    for (int j = 0; j < level.size(); ++j) {
                        final int size = level.get(j).color;
                        if (level.get(j).x < Client.this.left + 700 + size / 2 && level.get(j).x > Client.this.left - size / 2 && level.get(j).y < Client.this.top + 700 + size / 2 && level.get(j).y > Client.this.top - size / 2) {
                            g2.fillOval(level.get(j).x - size / 2 - Client.this.left, level.get(j).y - size / 2 - Client.this.top, size, size);
                        }
                    }
                }
                g2.setStroke(new BasicStroke(2.0f, 1, 1));
                for (int k = 0; k < Client.this.blobs.size(); ++k) {
                    final double[] spoints = Client.this.blobs.get(k).generate();
                    for (int i = 1; i < spoints.length / 3; ++i) {
                        g2.setColor(new Color(200 - Client.this.gen.nextInt(45), 230 - Client.this.gen.nextInt(45), 230 - Client.this.gen.nextInt(45)));
                        g2.drawLine((int)spoints[(i - 1) * 3] - Client.this.left, (int)spoints[(i - 1) * 3 + 1] - Client.this.top, (int)spoints[i * 3] - Client.this.left, (int)spoints[i * 3 + 1] - Client.this.top);
                    }
                }
                g2.setStroke(new BasicStroke(2.0f, 1, 1));
                for (int l = 0; l < Client.this.chains.size(); ++l) {
                    for (int m = 0; m < Client.this.chains.get(l).length - 1; ++m) {
                        g2.setColor(new Color(170 + Client.this.gen.nextInt(30), 190 + Client.this.gen.nextInt(30), 190 + Client.this.gen.nextInt(30)));
                        g2.drawLine((int)Client.this.chains.get(l).points[m][0] - Client.this.left, (int)Client.this.chains.get(l).points[m][1] - Client.this.top, (int)Client.this.chains.get(l).points[m + 1][0] - Client.this.left, (int)Client.this.chains.get(l).points[m + 1][1] - Client.this.top);
                    }
                }
                for (int l = 0; l < Client.this.circles.size(); ++l) {
                    final drawCircle c3 = Client.this.circles.get(l);
                    final int re = c3.getC().getRed();
                    final int gr = c3.getC().getGreen();
                    final int bl = c3.getC().getBlue();
                    if (c3.getX() + c3.getSize() / 2.0 > Client.this.left && c3.getX() - c3.getSize() / 2.0 < Client.this.left + Client.this.Field.getWidth() && c3.getY() + c3.getSize() / 2.0 > Client.this.top && c3.getY() - c3.getSize() / 2.0 < Client.this.top + Client.this.Field.getHeight()) {
                        g2.setColor(c3.getC());
                        g2.fillOval((int)(c3.getX() - c3.getSize() / 2.0 - Client.this.left), (int)(c3.getY() - c3.getSize() / 2.0 - Client.this.top), (int)c3.getSize(), (int)c3.getSize());
                        if (c3.id >= 0) {
                            g2.setColor(new Color(re + 20, gr + 20, bl + 20));
                            g2.fillOval((int)(c3.getX() - c3.getSize() / 2.0 - Client.this.left + 1.0), (int)(c3.getY() - c3.getSize() / 2.0 - Client.this.top + 1.0), (int)(c3.getSize() - 2.0), (int)(c3.getSize() - 2.0));
                            g2.setColor(new Color(re + 40, gr + 40, bl + 40));
                            g2.fillOval((int)(c3.getX() - c3.getSize() / 2.0 - Client.this.left + 2.0), (int)(c3.getY() - c3.getSize() / 2.0 - Client.this.top + 2.0), (int)(c3.getSize() - 4.0), (int)(c3.getSize() - 4.0));
                            if (c3.getSize() >= 10.0) {
                                for (int j2 = 0; j2 < c3.getSize(); j2 += 4) {
                                    g2.setColor(new Color(c3.getC().getRed() + Client.this.gen.nextInt(10) - Client.this.gen.nextInt(10), c3.getC().getGreen() + Client.this.gen.nextInt(10) - Client.this.gen.nextInt(10), c3.getC().getBlue() + Client.this.gen.nextInt(10) - Client.this.gen.nextInt(10)));
                                    g2.fillOval((int)(c3.getX() - Client.this.gen.nextDouble() * (c3.getSize() / 2.0) + 2.0 - Client.this.left), (int)(c3.getY() - Client.this.gen.nextDouble() * (c3.getSize() / 2.0) + 2.0 - Client.this.top), (int)(c3.getSize() / 4.0 + Client.this.gen.nextInt(2)), (int)(c3.getSize() / 4.0 + Client.this.gen.nextInt(2)));
                                }
                                for (int j2 = 0; j2 < c3.getSize(); j2 += 6) {
                                    g2.setColor(new Color(c3.getC().getRed() + Client.this.gen.nextInt(10) - Client.this.gen.nextInt(10), c3.getC().getGreen() + Client.this.gen.nextInt(10) - Client.this.gen.nextInt(10), c3.getC().getBlue() + Client.this.gen.nextInt(10) - Client.this.gen.nextInt(10)));
                                    g2.drawLine((int)(c3.getX() - Client.this.gen.nextDouble() * (c3.size / 2.0) + 4.0 - Client.this.left), (int)(c3.getY() - Client.this.gen.nextDouble() * (c3.getSize() / 2.0) + 4.0 - Client.this.top), (int)(c3.getX() + Client.this.gen.nextDouble() * (c3.size / 2.0) - 4.0 - Client.this.left), (int)(c3.getY() + Client.this.gen.nextDouble() * (c3.size / 2.0) - 4.0 - Client.this.top));
                                }
                            }
                        }
                        else if (re + 40 < 256 && gr + 40 < 256 && bl + 40 < 256) {
                            g2.setColor(new Color(re + 10, gr + 10, bl + 10));
                            g2.fillOval((int)(c3.getX() - c3.getSize() / 2.0 - Client.this.left + 2.0), (int)(c3.getY() - c3.getSize() / 2.0 - Client.this.top + 2.0), (int)(c3.getSize() - 4.0), (int)(c3.getSize() - 4.0));
                            g2.setColor(new Color(re + 20, gr + 20, bl + 20));
                            g2.fillOval((int)(c3.getX() - c3.getSize() / 2.0 - Client.this.left + 4.0), (int)(c3.getY() - c3.getSize() / 2.0 - Client.this.top + 4.0), (int)(c3.getSize() - 8.0), (int)(c3.getSize() - 8.0));
                            g2.setColor(new Color(re + 30, gr + 30, bl + 30));
                            g2.fillOval((int)(c3.getX() - c3.getSize() / 2.0 - Client.this.left + 6.0), (int)(c3.getY() - c3.getSize() / 2.0 - Client.this.top + 6.0), (int)(c3.getSize() - 12.0), (int)(c3.getSize() - 12.0));
                            g2.setColor(new Color(re + 40, gr + 40, bl + 40));
                            g2.fillOval((int)(c3.getX() - c3.getSize() / 2.0 - Client.this.left + 8.0), (int)(c3.getY() - c3.getSize() / 2.0 - Client.this.top + 8.0), (int)(c3.getSize() - 16.0), (int)(c3.getSize() - 16.0));
                        }
                        else {
                            g2.setColor(new Color(re - 10, gr - 10, bl - 10));
                            g2.fillOval((int)(c3.getX() - c3.getSize() / 2.0 - Client.this.left + 2.0), (int)(c3.getY() - c3.getSize() / 2.0 - Client.this.top + 2.0), (int)(c3.getSize() - 4.0), (int)(c3.getSize() - 4.0));
                            g2.setColor(new Color(re - 20, gr - 20, bl - 20));
                            g2.fillOval((int)(c3.getX() - c3.getSize() / 2.0 - Client.this.left + 4.0), (int)(c3.getY() - c3.getSize() / 2.0 - Client.this.top + 4.0), (int)(c3.getSize() - 8.0), (int)(c3.getSize() - 8.0));
                            g2.setColor(new Color(re - 30, gr - 30, bl - 30));
                            g2.fillOval((int)(c3.getX() - c3.getSize() / 2.0 - Client.this.left + 6.0), (int)(c3.getY() - c3.getSize() / 2.0 - Client.this.top + 6.0), (int)(c3.getSize() - 12.0), (int)(c3.getSize() - 12.0));
                            g2.setColor(new Color(re - 40, gr - 40, bl - 40));
                            g2.fillOval((int)(c3.getX() - c3.getSize() / 2.0 - Client.this.left + 8.0), (int)(c3.getY() - c3.getSize() / 2.0 - Client.this.top + 8.0), (int)(c3.getSize() - 16.0), (int)(c3.getSize() - 16.0));
                        }
                    }
                }
                for (int l = 0; l < Client.this.messages.size(); ++l) {
                    final Message m2 = Client.this.messages.get(l);
                    if (Client.this.messages.get(l).permanent) {
                        g2.setFont(new Font("Halvetica", 2, 22));
                        g2.setColor(Color.lightGray);
                        g2.drawString(Client.this.messages.get(l).message, 51, 51);
                        g2.setColor(Client.this.messages.get(l).c);
                        g2.drawString(Client.this.messages.get(l).message, 50, 50);
                        g2.setFont(new Font("Halvetica", 0, 12));
                    }
                    else if (System.currentTimeMillis() - m2.time < 3000L) {
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.drawString(Client.this.messages.get(l).message, 21, 21 + l * 20);
                        g2.setColor(Client.this.messages.get(l).c);
                        g2.drawString(Client.this.messages.get(l).message, 20, 20 + l * 20);
                    }
                    else {
                        Client.this.messages.remove(l);
                    }
                }
                if (Client.this.chart) {
                    for (int j3 = 0; j3 < Client.this.scores.size(); ++j3) {
                        g2.setColor(new Color(Client.this.scores.get(j3).get(0).color));
                        final ArrayList<ScorePoint> list = Client.this.scores.get(j3);
                        for (int i = 0; i < list.size() - 1; ++i) {
                            g2.drawLine(list.get(i).x * 500 / Client.this.time + 100, 600 - list.get(i).y * 500 / Client.this.maxScore, list.get(i + 1).x * 500 / Client.this.time + 100, 600 - list.get(i + 1).y * 500 / Client.this.maxScore);
                        }
                        g2.setColor(new Color(Client.this.scores.get(j3).get(0).color).brighter());
                        for (int i = 0; i < list.size() - 1; ++i) {
                            g2.drawLine(list.get(i).x * 500 / Client.this.time + 99, 599 - list.get(i).y * 500 / Client.this.maxScore, list.get(i + 1).x * 500 / Client.this.time + 99, 599 - list.get(i + 1).y * 500 / Client.this.maxScore);
                        }
                    }
                    g2.setFont(new Font("Halvetica", 0, 10));
                    g2.setColor(Color.BLACK);
                    for (int l = 0; l < 10; ++l) {
                        g2.drawString("" + (Client.this.maxScore - l * (Client.this.maxScore / 10)), 60, 100 + l * 50);
                    }
                    for (int l = 0; l < 11; ++l) {
                        g2.drawString("" + l * (Client.this.scores.get(0).get(Client.this.scores.get(0).size() - 1).x / 10), 100 + l * 50, 620);
                    }
                }
            }
            if (!Client.this.connected && !Client.this.chart) {
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                g2.setFont(new Font("Halvetica", 2, 80));
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawString("Cell Wars!!", 31, 81);
                g2.setColor(Color.RED);
                g2.drawString("Cell Wars!!", 30, 80);
                g2.drawImage(Client.this.loadTransImage("cell.png"), 30, 105, null);
                g2.setFont(new Font("Halvetica", 2, 80));
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Halvetica", 1, 16));
                g2.drawString("Welcome to CellWars by Max Shokhirev (c).", 30, 510);
                g2.setFont(new Font("Halvetica", 0, 12));
                g2.drawString("Start the game by selecting connect from the menu...", 30, 535);
                g2.drawString("Once connected, you will see your cell surrounded by a bunch of particles. If other people have joined, you must\n", 30, 550);
                g2.drawString("play until someone loses all of their cells. At first, your cells are immoble and cannot detect food (this is bad!).", 30, 565);
                g2.drawString("To evolve a trait in one of your cells, simply pick the trait from the list and click on one of your cells. You will\n", 30, 580);
                g2.drawString("need to have enough evolution points for each trait. As your cells grow, they will divide and die from old age. \n", 30, 595);
                g2.drawString("New cells will retain the properties of the cell they split from. It will be up to you to choose which traits to evolve\n", 30, 610);
                g2.drawString("and to choose which cells will recieve the benefit of a new trait. As time goes on, global events will occur that \n", 30, 625);
                g2.drawString("facilitate the game (i.e. pheremone trails, toxic contamination, and visits from killer alien cells). GLHF.", 30, 640);
                g2.setFont(new Font("Halvetica", 0, 22));
                g2.setColor(Color.RED);
                g2.drawString("Particles...", 550, 75);
                g2.setFont(new Font("Halvetica", 1, 16));
                g2.setColor(Color.WHITE);
                g2.drawString("Cell = ", 500, 110);
                int team = 0;
                int re2 = 123 * (team + 1) * ((team + 1) * 19) % 256;
                int gr2 = 23 * (team + 1) * ((team + 1) * 13) % 256;
                int bl2 = 51 * (team + 1) * ((team + 1) * 3) % 256;
                Point c4 = new Point(650, 100);
                Color col = new Color(re2 + gr2 * 256 + bl2 * 65536);
                re2 = col.getRed();
                gr2 = col.getGreen();
                bl2 = col.getBlue();
                final int size2 = 20;
                g2.setColor(new Color(re2 + 20, gr2 + 20, bl2 + 20));
                g2.fillOval((int)(c4.getX() - size2 / 2 + 1.0), (int)(c4.getY() - size2 / 2 + 1.0), size2 - 2, size2 - 2);
                g2.setColor(new Color(re2 + 40, gr2 + 40, bl2 + 40));
                g2.fillOval((int)(c4.getX() - size2 / 2 + 2.0), (int)(c4.getY() - size2 / 2 + 2.0), size2 - 4, size2 - 4);
                for (int j4 = 0; j4 < size2; j4 += 4) {
                    g2.setColor(new Color(re2 + Client.this.gen.nextInt(10) - Client.this.gen.nextInt(10), gr2 + Client.this.gen.nextInt(10) - Client.this.gen.nextInt(10), bl2 + Client.this.gen.nextInt(10) - Client.this.gen.nextInt(10)));
                    g2.fillOval((int)(c4.getX() - Client.this.gen.nextDouble() * (size2 / 2) + 2.0 - Client.this.left), (int)(c4.getY() - Client.this.gen.nextDouble() * (size2 / 2) + 2.0 - Client.this.top), size2 / 4 + Client.this.gen.nextInt(2), size2 / 4 + Client.this.gen.nextInt(2));
                }
                for (int j4 = 0; j4 < size2; j4 += 6) {
                    g2.setColor(new Color(re2 + Client.this.gen.nextInt(10) - Client.this.gen.nextInt(10), gr2 + Client.this.gen.nextInt(10) - Client.this.gen.nextInt(10), bl2 + Client.this.gen.nextInt(10) - Client.this.gen.nextInt(10)));
                    g2.drawLine((int)(c4.getX() - Client.this.gen.nextDouble() * (size2 / 2) + 4.0 - Client.this.left), (int)(c4.getY() - Client.this.gen.nextDouble() * (size2 / 2) + 4.0 - Client.this.top), (int)(c4.getX() + Client.this.gen.nextDouble() * (size2 / 2) - 4.0 - Client.this.left), (int)(c4.getY() + Client.this.gen.nextDouble() * (size2 / 2) - 4.0 - Client.this.top));
                }
                g2.setColor(Color.WHITE);
                g2.drawString("Food = ", 500, 160);
                team = -1;
                if (team < 0) {
                    team = -(team + 10);
                }
                re2 = 123 * (team + 1) * ((team + 1) * 19) % 256;
                gr2 = 23 * (team + 1) * ((team + 1) * 13) % 256;
                bl2 = 51 * (team + 1) * ((team + 1) * 3) % 256;
                c4 = new Point(650, 150);
                if (re2 + 40 < 256 && gr2 + 40 < 256 && bl2 + 40 < 256) {
                    g2.setColor(new Color(re2 + 10, gr2 + 10, bl2 + 10));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 2.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 2.0), size2 - 4, size2 - 4);
                    g2.setColor(new Color(re2 + 20, gr2 + 20, bl2 + 20));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 4.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 4.0), size2 - 8, size2 - 8);
                    g2.setColor(new Color(re2 + 30, gr2 + 30, bl2 + 30));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 6.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 6.0), size2 - 12, size2 - 12);
                    g2.setColor(new Color(re2 + 40, gr2 + 40, bl2 + 40));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 8.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 8.0), size2 - 16, size2 - 16);
                }
                else {
                    g2.setColor(new Color(re2 - 10, gr2 - 10, bl2 - 10));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 2.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 2.0), size2 - 4, size2 - 4);
                    g2.setColor(new Color(re2 - 20, gr2 - 20, bl2 - 20));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 4.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 4.0), size2 - 8, size2 - 8);
                    g2.setColor(new Color(re2 - 30, gr2 - 30, bl2 - 30));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 6.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 6.0), size2 - 12, size2 - 12);
                    g2.setColor(new Color(re2 - 40, gr2 - 40, bl2 - 40));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 8.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 8.0), size2 - 16, size2 - 16);
                }
                g2.setColor(Color.WHITE);
                g2.drawString("Inorganic = ", 500, 210);
                team = -2;
                if (team < 0) {
                    team = -(team + 10);
                }
                re2 = 123 * (team + 1) * ((team + 1) * 19) % 256;
                gr2 = 23 * (team + 1) * ((team + 1) * 13) % 256;
                bl2 = 51 * (team + 1) * ((team + 1) * 3) % 256;
                c4 = new Point(650, 200);
                if (re2 + 40 < 256 && gr2 + 40 < 256 && bl2 + 40 < 256) {
                    g2.setColor(new Color(re2 + 10, gr2 + 10, bl2 + 10));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 2.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 2.0), size2 - 4, size2 - 4);
                    g2.setColor(new Color(re2 + 20, gr2 + 20, bl2 + 20));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 4.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 4.0), size2 - 8, size2 - 8);
                    g2.setColor(new Color(re2 + 30, gr2 + 30, bl2 + 30));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 6.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 6.0), size2 - 12, size2 - 12);
                    g2.setColor(new Color(re2 + 40, gr2 + 40, bl2 + 40));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 8.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 8.0), size2 - 16, size2 - 16);
                }
                else {
                    g2.setColor(new Color(re2 - 10, gr2 - 10, bl2 - 10));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 2.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 2.0), size2 - 4, size2 - 4);
                    g2.setColor(new Color(re2 - 20, gr2 - 20, bl2 - 20));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 4.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 4.0), size2 - 8, size2 - 8);
                    g2.setColor(new Color(re2 - 30, gr2 - 30, bl2 - 30));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 6.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 6.0), size2 - 12, size2 - 12);
                    g2.setColor(new Color(re2 - 40, gr2 - 40, bl2 - 40));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 8.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 8.0), size2 - 16, size2 - 16);
                }
                g2.setColor(Color.WHITE);
                g2.drawString("Antibiotic(bad) = ", 500, 260);
                team = -3;
                if (team < 0) {
                    team = -(team + 10);
                }
                re2 = 123 * (team + 1) * ((team + 1) * 19) % 256;
                gr2 = 23 * (team + 1) * ((team + 1) * 13) % 256;
                bl2 = 51 * (team + 1) * ((team + 1) * 3) % 256;
                c4 = new Point(650, 250);
                if (re2 + 40 < 256 && gr2 + 40 < 256 && bl2 + 40 < 256) {
                    g2.setColor(new Color(re2 + 10, gr2 + 10, bl2 + 10));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 2.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 2.0), size2 - 4, size2 - 4);
                    g2.setColor(new Color(re2 + 20, gr2 + 20, bl2 + 20));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 4.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 4.0), size2 - 8, size2 - 8);
                    g2.setColor(new Color(re2 + 30, gr2 + 30, bl2 + 30));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 6.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 6.0), size2 - 12, size2 - 12);
                    g2.setColor(new Color(re2 + 40, gr2 + 40, bl2 + 40));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 8.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 8.0), size2 - 16, size2 - 16);
                }
                else {
                    g2.setColor(new Color(re2 - 10, gr2 - 10, bl2 - 10));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 2.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 2.0), size2 - 4, size2 - 4);
                    g2.setColor(new Color(re2 - 20, gr2 - 20, bl2 - 20));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 4.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 4.0), size2 - 8, size2 - 8);
                    g2.setColor(new Color(re2 - 30, gr2 - 30, bl2 - 30));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 6.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 6.0), size2 - 12, size2 - 12);
                    g2.setColor(new Color(re2 - 40, gr2 - 40, bl2 - 40));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 8.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 8.0), size2 - 16, size2 - 16);
                }
                g2.setColor(Color.WHITE);
                g2.drawString("Pheremone = ", 500, 310);
                team = -4;
                if (team < 0) {
                    team = -(team + 10);
                }
                re2 = 123 * (team + 1) * ((team + 1) * 19) % 256;
                gr2 = 23 * (team + 1) * ((team + 1) * 13) % 256;
                bl2 = 51 * (team + 1) * ((team + 1) * 3) % 256;
                col = new Color(re2 + gr2 * 256 + bl2 * 65536);
                re2 = col.getRed();
                gr2 = col.getGreen();
                bl2 = col.getBlue();
                c4 = new Point(650, 300);
                if (re2 + 40 < 256 && gr2 + 40 < 256 && bl2 + 40 < 256) {
                    g2.setColor(new Color(re2 + 10, gr2 + 10, bl2 + 10));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 2.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 2.0), size2 - 4, size2 - 4);
                    g2.setColor(new Color(re2 + 20, gr2 + 20, bl2 + 20));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 4.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 4.0), size2 - 8, size2 - 8);
                    g2.setColor(new Color(re2 + 30, gr2 + 30, bl2 + 30));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 6.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 6.0), size2 - 12, size2 - 12);
                    g2.setColor(new Color(re2 + 40, gr2 + 40, bl2 + 40));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 8.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 8.0), size2 - 16, size2 - 16);
                }
                else {
                    g2.setColor(new Color(re2 - 10, gr2 - 10, bl2 - 10));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 2.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 2.0), size2 - 4, size2 - 4);
                    g2.setColor(new Color(re2 - 20, gr2 - 20, bl2 - 20));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 4.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 4.0), size2 - 8, size2 - 8);
                    g2.setColor(new Color(re2 - 30, gr2 - 30, bl2 - 30));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 6.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 6.0), size2 - 12, size2 - 12);
                    g2.setColor(new Color(re2 - 40, gr2 - 40, bl2 - 40));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 8.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 8.0), size2 - 16, size2 - 16);
                }
                g2.setColor(Color.WHITE);
                g2.drawString("Alien = ", 500, 360);
                team = -5;
                if (team < 0) {
                    team = -(team + 10);
                }
                re2 = 123 * (team + 1) * ((team + 1) * 19) % 256;
                gr2 = 23 * (team + 1) * ((team + 1) * 13) % 256;
                bl2 = 51 * (team + 1) * ((team + 1) * 3) % 256;
                c4 = new Point(650, 350);
                col = new Color(re2 + gr2 * 256 + bl2 * 65536);
                re2 = col.getRed();
                gr2 = col.getGreen();
                bl2 = col.getBlue();
                if (re2 + 40 < 256 && gr2 + 40 < 256 && bl2 + 40 < 256) {
                    g2.setColor(new Color(re2 + 10, gr2 + 10, bl2 + 10));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 2.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 2.0), size2 - 4, size2 - 4);
                    g2.setColor(new Color(re2 + 20, gr2 + 20, bl2 + 20));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 4.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 4.0), size2 - 8, size2 - 8);
                    g2.setColor(new Color(re2 + 30, gr2 + 30, bl2 + 30));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 6.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 6.0), size2 - 12, size2 - 12);
                    g2.setColor(new Color(re2 + 40, gr2 + 40, bl2 + 40));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 8.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 8.0), size2 - 16, size2 - 16);
                }
                else {
                    g2.setColor(new Color(re2 - 10, gr2 - 10, bl2 - 10));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 2.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 2.0), size2 - 4, size2 - 4);
                    g2.setColor(new Color(re2 - 20, gr2 - 20, bl2 - 20));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 4.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 4.0), size2 - 8, size2 - 8);
                    g2.setColor(new Color(re2 - 30, gr2 - 30, bl2 - 30));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 6.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 6.0), size2 - 12, size2 - 12);
                    g2.setColor(new Color(re2 - 40, gr2 - 40, bl2 - 40));
                    g2.fillOval((int)(c4.getX() - size2 / 2 - Client.this.left + 8.0), (int)(c4.getY() - size2 / 2 - Client.this.top + 8.0), size2 - 16, size2 - 16);
                }
            }
        }
    }
}
