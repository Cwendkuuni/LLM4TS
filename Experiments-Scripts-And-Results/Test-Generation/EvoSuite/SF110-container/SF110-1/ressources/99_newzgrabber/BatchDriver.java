package Newzgrabber;

import java.io.*;
import java.util.*;

public class BatchDriver
{
    private Ini groupsini;
    private Ini batchini;
    private Hashtable NewsgroupsHash;
    private Hashtable BatchgroupsHash;
    private Hashtable BatchretrieveHash;
    private Hashtable OptionsHash;
    private Vector BatchgroupsVector;
    private String[] subjectlines;
    
    public BatchDriver(final String newzini, final String batchfile) throws NullPointerException, IOException {
        this.BatchgroupsVector = new Vector();
        final Ini groupsini = new Ini(newzini, "Messageids");
        final Ini batchgroups = new Ini(batchfile, "");
        this.NewsgroupsHash = groupsini.readIni();
        batchgroups.setProperty("Newsgroups");
        this.BatchgroupsHash = batchgroups.readIni();
        this.BatchgroupsVector = batchgroups.getIniVector();
        batchgroups.setProperty("Retrieve");
        this.BatchretrieveHash = batchgroups.readIni();
        batchgroups.setProperty("Options");
        this.OptionsHash = batchgroups.readIni();
    }
    
    public String[] getGroups() throws ArrayIndexOutOfBoundsException {
        final String[] groups = new String[this.BatchgroupsVector.size()];
        for (int i = 0; i < this.BatchgroupsVector.size(); ++i) {
            final String[] groupstuff = this.BatchgroupsVector.elementAt(i);
            groups[i] = groupstuff[0];
        }
        return groups;
    }
    
    public BatchJob getBatchJob(final String group) {
        try {
            String server = null;
            String directory = null;
            try {
                server = this.OptionsHash.get("server");
                directory = this.BatchgroupsHash.get(group).trim();
            }
            catch (Exception sdE) {
                System.out.println("Could not get server and directory for " + group);
                return null;
            }
            String username = null;
            String password = null;
            if (this.OptionsHash.containsKey("username")) {
                username = this.OptionsHash.get("username").trim();
            }
            if (this.OptionsHash.containsKey("password")) {
                password = this.OptionsHash.get("password").trim();
            }
            int nThreads = 1;
            int TIMEOUT = -1;
            try {
                nThreads = Integer.parseInt(this.OptionsHash.get("threads").trim());
            }
            catch (Exception threadsE) {
                nThreads = 1;
            }
            try {
                TIMEOUT = Integer.parseInt(this.OptionsHash.get("timeout").trim());
                Newzbatch.TIMEOUT = true;
                Newzbatch.TIMEOUTMAX = TIMEOUT;
                System.out.println("Setting the max timeout value to " + TIMEOUT);
            }
            catch (Exception threadsE) {
                TIMEOUT = -1;
            }
            if (Newzgrabber.verbose) {
                System.out.println("Getting NNTP object from BatchDriver with the server " + server);
            }
            final NewsFactory nf = Newzgrabber.nf;
            NewsFactory.setThreadLimit(nThreads);
            final NNTP news = Newzgrabber.nf.getNewsSocket(server);
            if (news == null) {
                System.out.println("Could not get news socket in BatchDriver");
                return null;
            }
            if (username != null && password != null) {
                news.authInfo(username, password);
            }
            final String[] groupinfo = news.genericCommand("group " + group);
            if (Newzgrabber.verbose) {
                System.out.println("Got the following group line from BatchDriver\n" + groupinfo[0]);
            }
            final Status thestatus = new Status(groupinfo[0].trim());
            if (!thestatus.statusOK()) {
                return null;
            }
            final StringTokenizer st = new StringTokenizer(groupinfo[0].trim());
            st.nextToken();
            st.nextToken();
            final int tmpFirst = Integer.parseInt(st.nextToken());
            final int tmpLast = Integer.parseInt(st.nextToken());
            if (Newzgrabber.IsBatch) {
                System.out.println("The first id in group is " + tmpFirst);
                System.out.println("The last id in group is " + tmpLast);
            }
            int firstid = 0;
            final int lastid = tmpLast;
            if (this.NewsgroupsHash.containsKey(group)) {
                try {
                    firstid = Integer.parseInt(this.NewsgroupsHash.get(group));
                    ++firstid;
                    if (Newzgrabber.IsBatch) {
                        System.out.println("ini file shows " + firstid + " as being the id");
                    }
                }
                catch (Exception firstidE) {
                    firstid = 0;
                }
            }
            if (tmpFirst > firstid) {
                firstid = tmpFirst;
            }
            final String cmd = "xpat Subject " + firstid + "-" + lastid + " *";
            System.out.println("Getting ready to get subjects with\n" + cmd);
            this.subjectlines = news.genericCommand(cmd);
            news.quit();
            final BatchJob bj = new BatchJob(server, group, directory, firstid, lastid);
            if (username != null && password != null) {
                bj.setUsername(username.trim());
                bj.setPassword(password.trim());
            }
            return bj;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public String[] getSubjectLines() {
        return this.subjectlines;
    }
}
