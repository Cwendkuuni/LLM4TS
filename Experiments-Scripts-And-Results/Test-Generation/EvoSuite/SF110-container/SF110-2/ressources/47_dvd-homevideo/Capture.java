import java.io.*;

public class Capture implements Runnable
{
    private int m_Minutes;
    private int m_Seconds;
    private GUI m_GUI;
    private Thread m_Thread;
    private boolean m_Error;
    private String m_BaseErr;
    private String m_dvgrab;
    
    public Capture() {
        this.m_BaseErr = "Capture Error - ";
        this.m_dvgrab = "dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-";
    }
    
    public Capture(final int min, final int sec, final GUI DVD_GUI) {
        this.m_BaseErr = "Capture Error - ";
        this.m_dvgrab = "dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-";
        this.m_Minutes = min;
        this.m_Seconds = sec;
        this.m_GUI = DVD_GUI;
    }
    
    public boolean init() {
        if (this.m_GUI.menuChkThread.isSelected()) {
            (this.m_Thread = new Thread(this)).setPriority(10);
            this.m_Thread.start();
            return false;
        }
        this.DV_Capture();
        return this.m_Error | this.m_GUI.ErrorCheck(this.m_GUI.strOutputDir + "/log/dvgrab.log");
    }
    
    @Override
    public void run() {
        this.DV_Capture();
    }
    
    public void DV_Capture() {
        File CreateOutDir = new File(this.m_GUI.strOutputDir);
        if (CreateOutDir.exists() && CreateOutDir.isDirectory()) {
            final int response = this.m_GUI.MessageBox("The Output Directory " + this.m_GUI.strOutputDir + " already exists!" + "\nWould you like to delete it?");
            if (response == 0) {
                try {
                    final String rm = "rm -fr " + this.m_GUI.strOutputDir;
                    final String[] rm_cmd = { "/bin/sh", "-c", rm };
                    final Process remove_dir = Runtime.getRuntime().exec(rm_cmd, null);
                    remove_dir.waitFor();
                }
                catch (IOException ex) {
                    SaveStackTrace.printTrace(this.m_GUI.strOutputDir, ex);
                    this.m_GUI.MessageBox(this.m_BaseErr + "IO Error\n" + ex.toString(), 0);
                    ex.printStackTrace();
                }
                catch (InterruptedException ex2) {
                    SaveStackTrace.printTrace(this.m_GUI.strOutputDir, ex2);
                    this.m_GUI.MessageBox(this.m_BaseErr + "dvd-homevideo thread was interrupted\n" + ex2.toString(), 0);
                    ex2.printStackTrace();
                }
            }
            else {
                this.m_GUI.MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2);
                this.m_GUI.blnBegin = false;
                this.m_Error = true;
            }
        }
        else if (CreateOutDir.exists() && !CreateOutDir.isDirectory()) {
            final int response = this.m_GUI.MessageBox("There exists a file with the Output directory name\nat this location!  Would you like to delete it?");
            if (response == 0) {
                try {
                    final String rm = "rm -fr " + this.m_GUI.strOutputDir;
                    final String[] rm_cmd = { "/bin/sh", "-c", rm };
                    final Process remove_dir = Runtime.getRuntime().exec(rm_cmd, null);
                    remove_dir.waitFor();
                }
                catch (IOException ex) {
                    SaveStackTrace.printTrace(this.m_GUI.strOutputDir, ex);
                    this.m_GUI.MessageBox(this.m_BaseErr + "IO Error\n" + ex.toString(), 0);
                    ex.printStackTrace();
                }
                catch (InterruptedException ex2) {
                    SaveStackTrace.printTrace(this.m_GUI.strOutputDir, ex2);
                    this.m_GUI.MessageBox(this.m_BaseErr + "dvd-homevideo thread was interrupted\n" + ex2.toString(), 0);
                    ex2.printStackTrace();
                }
            }
            else {
                this.m_GUI.MessageBox("Please choose a different name for your\ntitle or move the file to another location.", 2);
                this.m_GUI.blnBegin = false;
            }
        }
        if (this.m_GUI.blnBegin) {
            CreateOutDir.mkdir();
            CreateOutDir = new File(this.m_GUI.strOutputDir + "/DVD");
            CreateOutDir.mkdir();
            CreateOutDir = new File(this.m_GUI.strOutputDir + "/log");
            CreateOutDir.mkdir();
            CreateOutDir = new File(this.m_GUI.strOutputDir + "/dv");
            CreateOutDir.mkdir();
            try {
                final String[] dvgrab_cmd = { "/bin/sh", "-c", this.m_dvgrab };
                final Process p = Runtime.getRuntime().exec(dvgrab_cmd, null, new File(this.m_GUI.strOutputDir));
                final BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                final BufferedReader err_in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                final BufferedWriter out = new BufferedWriter(new FileWriter(this.m_GUI.strOutputDir + "/log/dvgrab.log"));
                final BufferedWriter err_out = new BufferedWriter(new FileWriter(this.m_GUI.strOutputDir + "/log/dvgrab.err"));
                if (err_in.ready()) {
                    final String line = err_in.readLine();
                    if (line.equals("/bin/sh: dvgrab: command not found")) {
                        this.m_GUI.MessageBox("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", 0);
                        in.close();
                        out.close();
                        err_in.close();
                        err_out.close();
                        this.m_Error = true;
                    }
                    else {
                        this.m_GUI.txtAreaOutput.append(line + "\n");
                        out.write(line);
                        out.newLine();
                    }
                }
                this.m_GUI.lblCapture.setEnabled(true);
                this.m_GUI.prgCapture.setEnabled(true);
                this.m_GUI.lblCaptureProg.setEnabled(true);
                final long start = System.currentTimeMillis();
                final int total = this.m_Minutes * 60 * 1000 + this.m_Seconds * 1000 + 5000;
                final long end = start + total;
                long current = System.currentTimeMillis();
                int diff = (int)(current - start);
                final int track = 0;
                while (diff / total < 1) {
                    this.m_GUI.prgCapture.setValue((int)(diff / total * 100.0));
                    this.m_GUI.lblCaptureProg.setText(Integer.toString((int)(diff / total * 100.0)) + "%");
                    current = System.currentTimeMillis();
                    diff = (int)(current - start);
                    String line = in.readLine();
                    if (err_in.ready()) {
                        line = err_in.readLine();
                        if (line.equals("Error: no camera exists")) {
                            this.m_GUI.MessageBox("It appears that your camcorder is not connected to your computer!", 0);
                            final String rm2 = "rm -fr " + this.m_GUI.strOutputDir;
                            final String[] rm_cmd2 = { "/bin/sh", "-c", rm2 };
                            final Process remove_dir2 = Runtime.getRuntime().exec(rm_cmd2, null);
                            remove_dir2.waitFor();
                            in.close();
                            out.close();
                            err_in.close();
                            err_out.close();
                            this.m_Error = true;
                        }
                        this.m_GUI.txtAreaOutput.append(line + "\n");
                        this.m_GUI.txtAreaOutput.setCaretPosition(this.m_GUI.txtAreaOutput.getDocument().getLength());
                        out.write(line);
                        out.newLine();
                    }
                }
                in.close();
                out.close();
                err_in.close();
                err_out.close();
                p.destroy();
                this.m_GUI.prgCapture.setValue(100);
                this.m_GUI.prgCapture.setValue(100);
                this.m_GUI.lblCaptureProg.setText("100%");
                this.m_GUI.prgCapture.setEnabled(false);
                this.m_GUI.lblCapture.setEnabled(false);
                this.m_GUI.lblCaptureProg.setEnabled(false);
                Thread.sleep(2000L);
            }
            catch (IOException ex3) {
                SaveStackTrace.printTrace(this.m_GUI.strOutputDir, ex3);
                this.m_GUI.MessageBox(this.m_BaseErr + "IO Error\n" + ex3.toString(), 0);
                ex3.printStackTrace();
                this.m_Error = true;
            }
            catch (NullPointerException ex4) {
                SaveStackTrace.printTrace(this.m_GUI.strOutputDir, ex4);
                this.m_GUI.MessageBox(this.m_BaseErr + "Error executing Runtime.getRuntime().exec()\n" + ex4.toString(), 0);
                ex4.printStackTrace();
                this.m_Error = true;
            }
            catch (IllegalArgumentException ex5) {
                SaveStackTrace.printTrace(this.m_GUI.strOutputDir, ex5);
                this.m_GUI.MessageBox(this.m_BaseErr + "Illegal argument sent to Runtime.getRuntime().exec()\n" + ex5.toString(), 0);
                ex5.printStackTrace();
                this.m_Error = true;
            }
            catch (Exception ex6) {
                SaveStackTrace.printTrace(this.m_GUI.strOutputDir, ex6);
                this.m_GUI.MessageBox(this.m_BaseErr + "Unknown Error occurred\n" + ex6.toString(), 0);
                ex6.printStackTrace();
                this.m_Error = true;
            }
        }
    }
}
