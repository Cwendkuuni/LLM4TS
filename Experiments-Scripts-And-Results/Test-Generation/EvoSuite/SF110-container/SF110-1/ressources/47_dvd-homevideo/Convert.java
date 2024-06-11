import java.util.*;
import java.io.*;

public class Convert implements Runnable
{
    private int m_Quality;
    private double m_fps;
    private String m_Format;
    private String m_AspectRatio;
    private String m_flags;
    private String m_bitrate;
    protected String[] video_files;
    private GUI m_GUI;
    private Thread m_Thread;
    private boolean m_Error;
    private int thread_track;
    private String m_BaseErr;
    private String m_transcode;
    private String mplex;
    
    public Convert(final String format, final String aspectRatio, final GUI DVD_GUI) {
        this.m_bitrate = "8500";
        this.m_BaseErr = "Transcoding Error - ";
        this.m_transcode = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
        this.mplex = "mplex -f 8 -V -o inp.vob inp.m2v inp.ac3";
        if (format.equals("dvd-ntsc")) {
            this.m_fps = 29.97;
        }
        else {
            this.m_fps = 25.0;
        }
        this.m_GUI = DVD_GUI;
        this.m_Format = format;
        this.m_AspectRatio = aspectRatio;
        this.thread_track = 0;
    }
    
    public Convert(final int quality, final String format, final String aspectRatio, final GUI DVD_GUI) {
        this.m_bitrate = "8500";
        this.m_BaseErr = "Transcoding Error - ";
        this.m_transcode = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
        this.mplex = "mplex -f 8 -V -o inp.vob inp.m2v inp.ac3";
        if (format.equals("dvd-ntsc")) {
            this.m_fps = 29.97;
        }
        else {
            this.m_fps = 25.0;
        }
        this.m_GUI = DVD_GUI;
        this.m_Quality = quality;
        this.m_Format = format;
        this.m_AspectRatio = aspectRatio;
        this.thread_track = 0;
    }
    
    public boolean init() {
        if (this.m_GUI.menuChkThread.isSelected()) {
            (this.m_Thread = new Thread(this)).start();
            try {
                this.m_Thread.join();
            }
            catch (InterruptedException ex) {
                SaveStackTrace.printTrace(this.m_GUI.strOutputDir, ex);
                this.m_GUI.MessageBox(this.m_BaseErr + "Could not join Convert.java thread\n" + ex.toString(), 0);
                ex.printStackTrace();
                this.m_Error = true;
            }
        }
        else {
            this.Transcode();
        }
        return this.m_Error || this.m_GUI.ErrorCheck(this.m_GUI.strOutputDir + "/log/transcode.log") || this.m_GUI.ErrorCheck(this.m_GUI.strOutputDir + "/log/mplex.log");
    }
    
    @Override
    public void run() {
        this.Transcode();
    }
    
    public void Transcode() {
        final File ListDir = new File(this.m_GUI.strOutputDir + "/dv/");
        if (this.m_Quality == 0) {
            this.m_flags = "'8, -c -q 2 -4 1 -2 1 -H -K file=matrix.txt -R 2'";
        }
        else if (this.m_Quality == 1) {
            this.m_flags = "'8, -c -q 4 -4 2 -2 2 -K file=matrix.txt -R 1'";
        }
        else {
            this.m_flags = "'8, -c -q 6 -4 3 -2 3 -N 0.5 -E -10 -K tmpgenc -R 0'";
        }
        this.m_transcode = this.m_transcode.replaceAll("flags", this.m_flags);
        this.m_transcode = this.m_transcode.replaceAll("bitr", this.m_bitrate);
        this.m_transcode = this.m_transcode.replaceAll("format", this.m_Format);
        this.m_transcode = this.m_transcode.replaceAll("aspectRatio", this.m_AspectRatio);
        this.m_transcode = this.m_transcode.replaceAll("frames/s", String.valueOf(this.m_fps));
        this.matrix();
        this.m_GUI.lblConvert.setEnabled(true);
        this.m_GUI.prgConvert.setEnabled(true);
        this.m_GUI.lblConvertProg.setEnabled(true);
        double cummulative_time = 0.0;
        double current_time = 0.0;
        final double[] average_fps = { 0.0, 0.0, 0.0, 0.0, -1.0 };
        final int total_time = (int)this.m_GUI.spnMinutes.getValue() * 60 + (int)this.m_GUI.spnSeconds.getValue();
        while (this.thread_track < ListDir.list().length) {
            this.video_files = ListDir.list();
            try {
                if (this.thread_track == 0) {
                    this.m_transcode = this.m_transcode.replaceAll("inp", this.video_files[this.thread_track]);
                    this.m_transcode = this.m_transcode.replaceAll("test1", this.video_files[this.thread_track].substring(0, 11));
                    this.mplex = this.mplex.replaceAll("inp", this.video_files[this.thread_track].substring(0, 11));
                }
                else {
                    this.m_transcode = this.m_transcode.replaceAll(this.video_files[this.thread_track - 1], this.video_files[this.thread_track]);
                    this.m_transcode = this.m_transcode.replaceAll(this.video_files[this.thread_track - 1].substring(0, 11), this.video_files[this.thread_track].substring(0, 11));
                    this.mplex = this.mplex.replaceAll(this.video_files[this.thread_track - 1].substring(0, 11), this.video_files[this.thread_track].substring(0, 11));
                }
            }
            catch (StringIndexOutOfBoundsException ex) {
                SaveStackTrace.printTrace(this.m_GUI.strOutputDir, ex);
                this.m_GUI.MessageBox(this.m_BaseErr + "Are there non-standard (not .dv)" + "\nfiles in the dv directory?\n" + ex.toString(), 0);
                ex.printStackTrace();
                this.m_Error = true;
            }
            try {
                final String[] transcode_cmd = { "/bin/sh", "-c", this.m_transcode };
                Process p = Runtime.getRuntime().exec(transcode_cmd, null, new File(this.m_GUI.strOutputDir));
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader err_in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                BufferedWriter out = new BufferedWriter(new FileWriter(this.m_GUI.strOutputDir + "/log/transcode.log"));
                BufferedWriter err_out = new BufferedWriter(new FileWriter(this.m_GUI.strOutputDir + "/log/transcode.err"));
                out.write(this.m_transcode);
                out.newLine();
                if (err_in.ready()) {
                    final String line = err_in.readLine();
                    if (line.equals("/bin/sh: transcode: command not found")) {
                        this.m_GUI.MessageBox("Could not locate transcode in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", 0);
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
                int k = 0;
                String line;
                while ((line = in.readLine()) != null) {
                    final StringTokenizer st = new StringTokenizer(line);
                    if (st.hasMoreTokens() && st.nextToken().equals("encoding")) {
                        st.nextToken();
                        st.nextToken();
                        average_fps[k % 5] = Double.valueOf(st.nextToken());
                        st.nextToken();
                        st.nextToken();
                        final StringTokenizer time = new StringTokenizer(st.nextToken(), ":,");
                        if (time.hasMoreTokens()) {
                            final int hour = Integer.valueOf(time.nextToken());
                            final int min = Integer.valueOf(time.nextToken());
                            final int sec = Integer.valueOf(time.nextToken());
                            current_time = hour * 3600 + min * 60 + sec + cummulative_time;
                            this.m_GUI.prgConvert.setValue((int)(current_time / total_time * 100.0));
                            this.m_GUI.lblConvertProg.setText(String.valueOf((int)(current_time / total_time * 100.0)) + "%");
                            double fps_sum = 0.0;
                            if (average_fps[4] != -1.0) {
                                for (int j = 0; j < 5; ++j) {
                                    fps_sum += average_fps[j];
                                }
                                fps_sum /= 5.0;
                                final int time_remaining = (int)((total_time - current_time) * (int)this.m_fps / fps_sum);
                                final int captureTipHour = time_remaining / 3600;
                                final int captureTipMinute = (time_remaining - 3600 * captureTipHour) / 60;
                                if (captureTipHour == 0 && captureTipMinute < 5) {
                                    this.m_GUI.prgConvert.setToolTipText("Less than 5 minutes remaining");
                                }
                                else if (captureTipMinute < 10) {
                                    this.m_GUI.prgConvert.setToolTipText(captureTipHour + ":0" + captureTipMinute + " Remaining");
                                }
                                else {
                                    this.m_GUI.prgConvert.setToolTipText(captureTipHour + ":" + captureTipMinute + " Remaining");
                                }
                            }
                            this.m_GUI.txtAreaOutput.append(line + "\n");
                            this.m_GUI.txtAreaOutput.setCaretPosition(this.m_GUI.txtAreaOutput.getDocument().getLength());
                            out.write(line);
                            out.newLine();
                        }
                        else {
                            this.m_GUI.txtAreaOutput.append(line + "\n");
                            this.m_GUI.txtAreaOutput.setCaretPosition(this.m_GUI.txtAreaOutput.getDocument().getLength());
                            out.write(line);
                            out.newLine();
                        }
                        ++k;
                    }
                }
                while ((line = err_in.readLine()) != null) {
                    this.m_GUI.txtAreaOutput.append(line + "\n");
                    this.m_GUI.txtAreaOutput.setCaretPosition(this.m_GUI.txtAreaOutput.getDocument().getLength());
                    out.write(line);
                    out.newLine();
                }
                in.close();
                out.close();
                err_in.close();
                err_out.close();
                cummulative_time = current_time;
                final String[] mplex_cmd = { "/bin/sh", "-c", this.mplex };
                p = Runtime.getRuntime().exec(mplex_cmd, null, new File(this.m_GUI.strOutputDir));
                in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                err_in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                out = new BufferedWriter(new FileWriter(this.m_GUI.strOutputDir + "/log/mplex.log"));
                err_out = new BufferedWriter(new FileWriter(this.m_GUI.strOutputDir + "/log/mplex.err"));
                if (err_in.ready()) {
                    line = err_in.readLine();
                    if (line.equals("/bin/sh: mplex: command not found")) {
                        this.m_GUI.MessageBox("Could not locate mplex in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", 0);
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
                while ((line = err_in.readLine()) != null) {
                    this.m_GUI.txtAreaOutput.append(line + "\n");
                    this.m_GUI.txtAreaOutput.setCaretPosition(this.m_GUI.txtAreaOutput.getDocument().getLength());
                    out.write(line);
                    out.newLine();
                }
                in.close();
                out.close();
                err_in.close();
                err_out.close();
                Thread.sleep(2000L);
            }
            catch (IOException ex2) {
                SaveStackTrace.printTrace(this.m_GUI.strOutputDir, ex2);
                this.m_GUI.MessageBox(this.m_BaseErr + "IO Error\n" + ex2.toString(), 0);
                ex2.printStackTrace();
                this.m_Error = true;
            }
            catch (NullPointerException ex3) {
                SaveStackTrace.printTrace(this.m_GUI.strOutputDir, ex3);
                this.m_GUI.MessageBox(this.m_BaseErr + "Error executing Runtime.getRuntime().exec()\n" + ex3.toString(), 0);
                ex3.printStackTrace();
                this.m_Error = true;
            }
            catch (IllegalArgumentException ex4) {
                SaveStackTrace.printTrace(this.m_GUI.strOutputDir, ex4);
                this.m_GUI.MessageBox(this.m_BaseErr + "Illegal argument sent to Runtime.getRuntime().exec()\n" + ex4.toString(), 0);
                ex4.printStackTrace();
                this.m_Error = true;
            }
            catch (Exception ex5) {
                SaveStackTrace.printTrace(this.m_GUI.strOutputDir, ex5);
                this.m_GUI.MessageBox(this.m_BaseErr + "Unknown Error occurred\n" + ex5.toString(), 0);
                ex5.printStackTrace();
                this.m_Error = true;
            }
            ++this.thread_track;
        }
        this.m_GUI.prgConvert.setValue(100);
        this.m_GUI.lblConvertProg.setText("100%");
        this.m_GUI.prgConvert.setEnabled(false);
        this.m_GUI.lblConvert.setEnabled(false);
        this.m_GUI.lblConvertProg.setEnabled(false);
    }
    
    public void matrix() {
        try {
            final FileWriter fw = new FileWriter(this.m_GUI.strOutputDir + "/" + "matrix.txt");
            final BufferedWriter wr = new BufferedWriter(fw);
            wr.write("# High resolution INTRA table\n");
            wr.write("8,16,18,20,24,25,26,30\n");
            wr.write("16,16,20,23,25,26,30,30\n");
            wr.write("18,20,22,24,26,28,29,31\n");
            wr.write("20,21,23,24,26,28,31,31\n");
            wr.write("21,23,24,25,28,30,30,33\n");
            wr.write("23,24,25,28,30,30,33,36\n");
            wr.write("24,25,26,29,29,31,34,38\n");
            wr.write("25,26,28,29,31,34,38,42\n");
            wr.write("# TMPEGEnc NON-INTRA table\n");
            wr.write("16,17,18,19,20,21,22,23\n");
            wr.write("17,18,19,20,21,22,23,24\n");
            wr.write("18,19,20,21,22,23,24,25\n");
            wr.write("19,20,21,22,23,24,26,27\n");
            wr.write("20,21,22,23,25,26,27,28\n");
            wr.write("21,22,23,24,26,27,28,30\n");
            wr.write("22,23,24,26,27,28,30,31\n");
            wr.write("23,24,25,27,28,30,31,33\n");
            wr.close();
            fw.close();
        }
        catch (IOException ex) {
            SaveStackTrace.printTrace(this.m_GUI.strOutputDir, ex);
            this.m_GUI.MessageBox(this.m_BaseErr + "IO Error\n" + ex.toString(), 0);
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
