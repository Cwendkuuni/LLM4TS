package Newzgrabber;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Newzgrabber extends JFrame implements WindowListener
{
    public static JTabbedPane jtp;
    public static OptionsPanel optionspanel;
    public static GroupsPanel groupspanel;
    public static SearchPanel searchpanel;
    public static boolean verbose;
    public static boolean debug;
    public static boolean batch;
    public static TransferPanel transferpanel;
    public static boolean IsBatch;
    public static NewsFactory nf;
    private Vector affiliates;
    public static String Newzdirectory;
    public static String JarFilename;
    public static String BatchFilename;
    public static String NewzInifilename;
    
    public Newzgrabber() {
        Newzgrabber.IsBatch = true;
        setupBasicSystem();
        try {
            final String batchfile = Newzgrabber.Newzdirectory + System.getProperty("file.separator") + Newzgrabber.BatchFilename;
            final String newzini = Newzgrabber.Newzdirectory + System.getProperty("file.separator") + Newzgrabber.NewzInifilename;
            if (Newzgrabber.verbose) {
                System.out.println("Getting batch info from " + batchfile);
            }
            final BatchDriver bd = new BatchDriver(newzini, batchfile);
            final String[] BatchGroups = bd.getGroups();
            final Vector GroupDownloadCount = new Vector();
            for (int i = 0; i < BatchGroups.length; ++i) {
                System.out.println("Starting batch job for " + BatchGroups[i]);
                final BatchJob bj = bd.getBatchJob(BatchGroups[i]);
                if (bj == null) {
                    System.out.println("Returned NULL batch job object for " + BatchGroups[i]);
                }
                else {
                    if (Newzgrabber.verbose) {
                        System.out.println("Got batch job object");
                    }
                    final String[] grouparray = { bj.getGroup() };
                    if (Newzgrabber.verbose) {
                        System.out.println("Getting the line data");
                    }
                    final LineData ld = new LineData(bj.getServer(), grouparray);
                    if (Newzgrabber.verbose) {
                        System.out.println("Done getting line data");
                    }
                    final String[] subjectlines = bd.getSubjectLines();
                    final SongList sl = new SongList(subjectlines);
                    final String[] complete = sl.getCompleteSongs();
                    int nDownloads = 0;
                    final Vector FullList = new Vector();
                    if (Newzgrabber.verbose) {
                        System.out.println("Found " + complete.length + " articles");
                    }
                    for (int j = 0; j < complete.length; ++j) {
                        try {
                            final Object[] va = { bj.getGroup(), sl.getSongs().get(complete[j]) };
                            final Hashtable tmp = (Hashtable)va[1];
                            final long[] OrderedIds = SongInfo.getOrderedIds(tmp);
                            if (OrderedIds != null) {
                                final long TotalLines = ld.getTotalLines(BatchGroups[i], OrderedIds);
                                if (tmp.get("UPPERLIMIT") < 2) {
                                    final SongInfo si = tmp.get(new Integer(1));
                                    final long id = si.getId();
                                    final long[] tmpids = { id };
                                    if (ld.getTotalLines(BatchGroups[i], tmpids) < 100L) {
                                        continue;
                                    }
                                }
                                final Object[] rowinfo = { complete[j], BatchGroups[i], new Long(TotalLines), OrderedIds };
                                FullList.add(rowinfo);
                            }
                        }
                        catch (Exception completeE) {
                            System.out.println(completeE.getMessage());
                        }
                    }
                    for (int downindex = 0; downindex < FullList.size(); ++downindex) {
                        try {
                            final Object[] objarr = FullList.elementAt(downindex);
                            final Downloader d = new Downloader();
                            d.setDownloadDirectory(new File(bj.getDirectory()));
                            d.setServer(bj.getServer());
                            if (bj.getUsername() != null) {
                                d.setUsername(bj.getUsername().trim());
                            }
                            if (bj.getPassword() != null) {
                                d.setPassword(bj.getPassword().trim());
                            }
                            d.setGroup(BatchGroups[i]);
                            d.setIds((long[])objarr[3]);
                            d.setTotalLines((long)objarr[2]);
                            final Object[] addrow = { (String)objarr[0], new String(), new String(), new String(), BatchGroups[i] };
                            d.startDownload();
                            ++nDownloads;
                        }
                        catch (Exception downloadE) {
                            System.out.println(downloadE.getMessage());
                        }
                    }
                    final Object[] DownloadData = { BatchGroups[i], new Integer(nDownloads) };
                    GroupDownloadCount.add(DownloadData);
                    this.updateIni(newzini, "Messageids", BatchGroups[i], String.valueOf(bj.getLastid()));
                }
            }
            System.out.println("The following is the final download data:");
            System.out.println("GROUP\tDOWNLOADS");
            for (int cnt = 0; cnt < GroupDownloadCount.size(); ++cnt) {
                final Object[] downarr = GroupDownloadCount.elementAt(cnt);
                final String tmpGroup = (String)downarr[0];
                final int tmpDownloads = (int)downarr[1];
                System.out.println(tmpGroup + "\t" + tmpDownloads);
            }
        }
        catch (Exception batchE) {
            System.out.println("Caught exception");
            System.out.println(batchE.getMessage());
            System.exit(1);
        }
    }
    
    public Newzgrabber(final String s) {
        super(s);
        Newzgrabber.IsBatch = false;
        this.getContentPane().setLayout(new BorderLayout());
        this.addWindowListener(this);
        this.setSize(600, 500);
        setupBasicSystem();
        Newzgrabber.searchpanel = new SearchPanel(this);
        Newzgrabber.optionspanel = new OptionsPanel(this);
        Newzgrabber.groupspanel = new GroupsPanel(this);
        Newzgrabber.transferpanel = new TransferPanel(this);
        final AboutPanel p4 = new AboutPanel();
        (Newzgrabber.jtp = new JTabbedPane()).addTab("Search", Newzgrabber.searchpanel);
        Newzgrabber.jtp.setSelectedIndex(0);
        Newzgrabber.jtp.addTab("Options", Newzgrabber.optionspanel);
        Newzgrabber.jtp.setSelectedIndex(1);
        Newzgrabber.jtp.addTab("Groups", Newzgrabber.groupspanel);
        Newzgrabber.jtp.setSelectedIndex(2);
        Newzgrabber.jtp.addTab("Transfer", Newzgrabber.transferpanel);
        Newzgrabber.jtp.setSelectedIndex(3);
        Newzgrabber.jtp.addTab("About", p4);
        Newzgrabber.jtp.setSelectedIndex(4);
        Newzgrabber.jtp.setPreferredSize(new Dimension(this.getWidth() - 100, this.getHeight() - 100));
        this.getContentPane().add(Newzgrabber.jtp, "Center");
        this.setVisible(true);
        Newzgrabber.jtp.setSelectedIndex(0);
    }
    
    public static void setupBasicSystem() {
        Newzgrabber.Newzdirectory = findDirectory(Newzgrabber.JarFilename);
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1) {
            try {
                System.setProperty("java.library.path", Newzgrabber.Newzdirectory + System.getProperty("path.separator") + System.getProperty("java.library.path"));
            }
            catch (Exception pathE) {
                if (Newzgrabber.debug) {
                    System.err.println("There was a problem setting the lib path!");
                    pathE.printStackTrace();
                }
            }
            if (Newzgrabber.debug || Newzgrabber.verbose) {
                System.out.println("The path variable is set to\n" + System.getProperty("java.library.path"));
            }
        }
        if (Newzgrabber.verbose) {
            System.out.println("The newz dir is\n" + Newzgrabber.Newzdirectory);
        }
        Newzgrabber.nf = new NewsFactory();
    }
    
    @Override
    public void windowClosed(final WindowEvent we) {
    }
    
    @Override
    public void windowOpened(final WindowEvent we) {
    }
    
    @Override
    public void windowActivated(final WindowEvent we) {
    }
    
    @Override
    public void windowDeactivated(final WindowEvent we) {
    }
    
    @Override
    public void windowIconified(final WindowEvent we) {
    }
    
    @Override
    public void windowDeiconified(final WindowEvent we) {
    }
    
    @Override
    public void windowClosing(final WindowEvent we) {
        try {
            Newzgrabber.optionspanel.updateIni();
            Newzgrabber.groupspanel.updateIni();
            Newzgrabber.transferpanel.updateIni();
        }
        catch (Exception ce) {
            System.err.println(ce.getMessage());
            ce.printStackTrace();
        }
        System.exit(0);
    }
    
    public static String findDirectory(final String classfile) {
        try {
            final String path = System.getProperty("java.class.path");
            final StringTokenizer st = new StringTokenizer(path, System.getProperty("path.separator"));
            while (st.hasMoreTokens()) {
                final String nextpath = st.nextToken();
                try {
                    final File f = new File(nextpath);
                    if (!f.isFile() || !f.getName().equals(classfile)) {
                        continue;
                    }
                    final File parent = f.getParentFile();
                    if (parent == null) {
                        continue;
                    }
                    return parent.getAbsolutePath();
                }
                catch (Exception parseE) {
                    System.err.println("There was a parse error!\n" + parseE.getMessage());
                    parseE.printStackTrace();
                }
            }
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public void updateIni(final String inifile, final String prop, final String key, final String value) {
        try {
            final LinkedList ll = new LinkedList();
            if (Newzgrabber.verbose) {
                System.out.println("Updating the ini file from Newzgrabber");
            }
            final Ini ini = new Ini(inifile, prop);
            final Vector Newids = ini.getIniVector();
            if (Newids.size() > 0) {
                for (int i = 0; i < Newids.size(); ++i) {
                    final String[] stringarr = Newids.elementAt(i);
                    final String nextkey = stringarr[0];
                    String thevalue = null;
                    try {
                        if (nextkey.toLowerCase().equals(key.toLowerCase())) {
                            thevalue = value;
                        }
                        else {
                            thevalue = stringarr[1];
                        }
                        final String[] listobj = { nextkey, thevalue };
                        System.out.println("Adding " + nextkey + " to hash with " + thevalue);
                        ll.add(listobj);
                    }
                    catch (Exception lle) {
                        System.out.println("Problem updating ini file!\n" + lle.getMessage());
                    }
                }
            }
            ini.writeIni(ll);
        }
        catch (Exception e) {
            System.out.println("Problem updating ini file!\n" + e.getMessage());
        }
    }
    
    public static void main(final String[] args) {
        try {
            System.out.println("The user directory is:\n" + System.getProperty("user.dir"));
            if (args.length > 0) {
                for (int i = 0; i < args.length; ++i) {
                    if (args[i].toLowerCase().equals("-verbose")) {
                        Newzgrabber.verbose = true;
                        System.out.println("Running Newzgrabber in verbose mode");
                    }
                    else if (args[i].toLowerCase().equals("-batch")) {
                        Newzgrabber.IsBatch = true;
                        System.out.println("Running Newzgrabber in batch mode");
                    }
                    else if (args[i].toLowerCase().equals("-debug")) {
                        Newzgrabber.debug = true;
                        System.out.println("Running Newzgrabber in debug mode");
                    }
                    else if (args[i].toLowerCase().equals("-batch")) {
                        Newzgrabber.batch = true;
                        System.out.println("Running Newzgrabber in batch mode");
                    }
                }
            }
            Newzgrabber n = null;
            Newzbatch nb = null;
            if (Newzgrabber.IsBatch) {
                nb = new Newzbatch();
            }
            else {
                n = new Newzgrabber("Newzgrabber");
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    static {
        Newzgrabber.verbose = false;
        Newzgrabber.debug = false;
        Newzgrabber.batch = false;
        Newzgrabber.IsBatch = false;
        Newzgrabber.JarFilename = "Newz.jar";
        Newzgrabber.BatchFilename = "batch.ini";
        Newzgrabber.NewzInifilename = "Newzgrabber.ini";
    }
}
