package Newzgrabber;

import java.util.*;
import java.io.*;

public class Downloader
{
    private String Group;
    private NNTP news;
    private BufferedCustomInputStream br;
    private PrintWriter pw;
    private String Server;
    protected static final int UUEncoded = 1;
    protected static final int Base64Encoded = 2;
    protected static final int yEncoded = 3;
    private int EncType;
    private String Filename;
    protected static int MARKLIMIT;
    private long[] ids;
    private long TotalLines;
    private String Title;
    private String Username;
    private String Password;
    private File DownloadDirectory;
    private Object[] DownloadData;
    private int TableIndex;
    private static int TIMEOUTCOUNT;
    private int checkcount;
    private boolean NothingFound;
    private FileOutputStream fos;
    private boolean IsWindows;
    private int TIMEOUT;
    public boolean ABORT;
    
    public Downloader() {
        this.NothingFound = false;
        this.IsWindows = true;
        this.TIMEOUT = -1;
        this.ABORT = false;
        try {
            this.IsWindows = (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1);
            if (Newzgrabber.optionspanel.DirectoryText != null && Newzgrabber.optionspanel.DirectoryText.getText().trim().length() > 0) {
                this.DownloadDirectory = new File(Newzgrabber.optionspanel.DirectoryText.getText().trim());
                if (!this.DownloadDirectory.isDirectory()) {
                    this.DownloadDirectory = null;
                }
            }
        }
        catch (Exception e) {
            this.DownloadDirectory = null;
        }
        this.checkcount = 0;
        this.EncType = 0;
        this.TableIndex = -1;
    }
    
    public void setTimeout(final int i) {
        this.TIMEOUT = i;
    }
    
    public int getTimeout() {
        return this.TIMEOUT;
    }
    
    public void setDownloadDirectory(final File f) {
        this.DownloadDirectory = f;
    }
    
    public void setTableIndex(final int s) {
        this.TableIndex = s;
    }
    
    public void setServer(final String s) {
        this.Server = s;
    }
    
    public void setIds(final long[] ids) {
        this.ids = ids;
    }
    
    public void setTotalLines(final long l) {
        this.TotalLines = l;
    }
    
    public long getTotalLines() {
        return this.TotalLines;
    }
    
    public void setTitle(final String t) {
        this.Title = t;
    }
    
    public void setUsername(final String u) {
        this.Username = u;
    }
    
    public void setPassword(final String p) {
        this.Password = p;
    }
    
    public void setGroup(final String g) {
        this.Group = g;
    }
    
    public String getGroup() {
        return this.Group;
    }
    
    public long getBaseMessageId() {
        if (this.ids != null) {
            return this.ids[0];
        }
        return -1L;
    }
    
    public void setDownloadData(final Object[] obj) {
        this.DownloadData = obj;
    }
    
    public void close() {
        if (Newzgrabber.verbose) {
            if (this.Filename != null) {
                System.out.println("Closing the downloader object for " + this.Filename);
            }
            else {
                System.out.println("Closing the downloader object for NULL Filename");
            }
        }
        try {
            if (this.fos != null) {
                this.fos.close();
            }
            else if (Newzgrabber.verbose) {
                System.out.println("The output stream was null when closing the news object!");
            }
        }
        catch (Exception fe) {
            if (Newzgrabber.verbose) {
                System.out.println("There was a problem closing the filehandle\n" + fe.getMessage());
            }
        }
        try {
            if (this.news != null && this.news.isOpen()) {
                this.news.quit();
            }
        }
        catch (Exception ne) {
            if (Newzgrabber.verbose) {
                System.out.println("There was a problem closing the news socket object!\n" + ne.getMessage());
            }
        }
    }
    
    public void startDownload() throws IOException {
        try {
            if (Newzgrabber.verbose) {
                System.out.println("Started download!");
            }
            String FullFilename = null;
            if (this.ids.length < 1) {
                return;
            }
            if (Newzgrabber.verbose) {
                System.out.println("Attempting to connect to " + this.Server);
            }
            this.news = Newzgrabber.nf.getNewsSocket(this.Server);
            if (Newzgrabber.verbose) {
                System.out.println("Created NNTP object");
            }
            if (this.Username != null && this.Password != null && Newzgrabber.verbose) {
                System.out.println("Authorizing " + this.Username + " with " + this.Password);
            }
            if (Newzgrabber.verbose) {
                System.out.println("Trying to get the PrintWriter");
            }
            this.pw = this.news.getWriter();
            if (Newzgrabber.verbose) {
                System.out.println("Trying to get the BufferedReader");
            }
            this.br = this.news.getReader();
            if (Newzgrabber.verbose) {
                System.out.println("Got them both");
            }
            while (this.br.ready()) {
                if (Newzgrabber.verbose) {
                    System.out.println("Reader is ready...trying to read line");
                }
                final String NextLine = this.br.readLine();
                if (Newzgrabber.verbose) {
                    System.out.println("Flushing '" + NextLine + "'");
                }
            }
            if (Newzgrabber.verbose) {
                System.out.println("Sending the group command");
            }
            if (this.IsWindows) {
                this.pw.println("group " + this.Group);
            }
            else {
                this.pw.print("group " + this.Group + "\r\n");
            }
            this.pw.flush();
            if (Newzgrabber.verbose) {
                System.out.println("Set the group command to " + this.Group);
            }
            final String groupcmd = this.br.readLine();
            if (Newzgrabber.verbose) {
                System.out.println("The groupcmd returned was " + groupcmd);
            }
            if (Newzgrabber.verbose) {
                System.out.println(groupcmd);
            }
            MimeDecoder md = null;
            this.fos = null;
            final String NextLine2 = null;
            boolean FileFound = false;
            boolean STATOK = false;
            if (Newzgrabber.verbose) {
                System.out.println("The total number of lines is: " + this.TotalLines);
            }
            long cnt = 0L;
            int loopcount = -1;
            int i = 0;
            while (i < this.ids.length) {
                ++loopcount;
                if (this.ABORT) {
                    md.ABORT = true;
                    break;
                }
                if (Newzgrabber.verbose) {
                    System.out.println("Getting data from id " + this.ids[i]);
                }
                if (this.IsWindows) {
                    this.pw.println("stat " + this.ids[i]);
                }
                else {
                    this.pw.print("stat " + this.ids[i] + "\r\n");
                }
                this.pw.flush();
                if (Newzgrabber.verbose) {
                    System.out.println("Checking if " + this.br.toString() + " is ready...");
                }
                while (!this.br.ready()) {
                    if (Newzgrabber.verbose) {
                        System.out.println("Stat line is not ready to read yet");
                    }
                    try {
                        Thread.sleep(100L);
                    }
                    catch (Exception te) {}
                }
                final String statline = this.br.readLine();
                if (Newzgrabber.verbose) {
                    System.out.println("The stat line for " + this.ids[i] + " was\n" + statline);
                }
                try {
                    final StringTokenizer stst = new StringTokenizer(statline);
                    final int tmpstatus = Integer.parseInt(stst.nextToken());
                    STATOK = (tmpstatus >= 200 && tmpstatus < 300);
                }
                catch (Exception statE) {
                    STATOK = false;
                }
                if (!STATOK) {
                    if (Newzgrabber.verbose) {
                        System.out.println("Could not get stat line for " + this.ids[i]);
                        break;
                    }
                    break;
                }
                else {
                    final String tmpfilename = this.DownloadDirectory.getAbsolutePath() + System.getProperty("file.separator") + this.Group.replace('.', '_') + "." + this.ids[i];
                    if (Newzgrabber.verbose) {
                        System.out.println("Just sent the body command");
                    }
                    if (this.IsWindows) {
                        this.pw.println("body");
                    }
                    else {
                        this.pw.print("body\r\n");
                    }
                    this.pw.flush();
                    Label_1561: {
                        if (this.Filename == null) {
                            if (Newzgrabber.verbose) {
                                System.out.println("Getting ready to look for the filename");
                            }
                            FileFound = this.findFile();
                            if (Newzgrabber.verbose) {
                                System.out.println("The value of FileFound is " + FileFound);
                            }
                            if (FileFound) {
                                if (Newzgrabber.verbose) {
                                    System.out.println("Found a filename!");
                                }
                                if (!Newzgrabber.IsBatch && this.TableIndex != -1) {
                                    TransferPanel.downloading.setValueAt(this.Filename, this.TableIndex, 0);
                                }
                                if (this.DownloadDirectory != null) {
                                    FullFilename = this.DownloadDirectory.getAbsolutePath() + System.getProperty("file.separator") + this.Filename;
                                }
                                else {
                                    FullFilename = this.Filename;
                                }
                                FullFilename = NewsFile.renameFile(FullFilename);
                                this.fos = new FileOutputStream(FullFilename);
                                if (!Newzgrabber.IsBatch && this.TableIndex != -1) {
                                    if (this.EncType == 1) {
                                        TransferPanel.downloading.setValueAt("UU Decode", this.TableIndex, 2);
                                    }
                                    else if (this.EncType == 2) {
                                        TransferPanel.downloading.setValueAt("Base64 Decode", this.TableIndex, 2);
                                    }
                                    else if (this.EncType == 3) {
                                        TransferPanel.downloading.setValueAt("yEnc Decode", this.TableIndex, 2);
                                    }
                                    else {
                                        TransferPanel.downloading.setValueAt("Unknown", this.TableIndex, 2);
                                    }
                                }
                                if (Newzgrabber.verbose) {
                                    System.out.println("Got full filename of " + FullFilename);
                                }
                            }
                            else {
                                if (Newzgrabber.verbose) {
                                    System.out.println("Could not find the filename");
                                }
                                if (this.checkcount > Downloader.TIMEOUTCOUNT) {
                                    this.NothingFound = true;
                                    break;
                                }
                                break Label_1561;
                            }
                        }
                        if (this.EncType == 1) {
                            if (Newzgrabber.verbose) {
                                System.out.println("Trying a UU decode");
                            }
                            md = new UUDecoder(this.br, this.fos);
                        }
                        else if (this.EncType == 2) {
                            if (Newzgrabber.verbose) {
                                System.out.println("Trying a Base64 decode");
                            }
                            md = new Base64Decoder(this.br, this.fos);
                        }
                        else if (this.EncType == 3) {
                            if (Newzgrabber.verbose) {
                                System.out.println("Trying a yEnc decode");
                            }
                            md = new yEncDecoder(this.br, this.fos);
                            if (loopcount == 0) {
                                md.setBegin(true);
                            }
                            if (Newzgrabber.verbose) {
                                System.out.println("Got new yEnc object");
                            }
                        }
                        if (!Newzgrabber.IsBatch) {
                            md.setStatusRow(this.TableIndex);
                            md.setTotalLines(this.TotalLines);
                            md.setLineCount(cnt);
                        }
                        if (Newzbatch.TIMEOUT) {
                            Newzbatch.resetCount();
                        }
                        if (Newzgrabber.verbose) {
                            System.out.println("Calling the decodeStream() for object");
                        }
                        md.decodeStream();
                        cnt = md.getLineCount();
                    }
                    ++i;
                }
            }
            try {
                if (Newzgrabber.verbose) {
                    System.out.println("Shutting down news object");
                }
                this.news.quit();
                this.fos.close();
            }
            catch (Exception ce) {
                if (Newzgrabber.verbose) {
                    System.err.println("Could not close filehandle!\n" + ce.getMessage());
                }
            }
            if (Newzgrabber.verbose) {
                System.out.println("Done downloading " + this.Filename);
            }
            if (this.ABORT && !Newzgrabber.IsBatch) {
                TransferPanel.downloading.setValueAt("Aborted", this.TableIndex, 1);
            }
            else if (!STATOK) {
                try {
                    final File ff = new File(FullFilename);
                    ff.delete();
                }
                catch (Exception ex) {}
                if (!Newzgrabber.IsBatch) {
                    TransferPanel.downloading.setValueAt("Article Not Found", this.TableIndex, 1);
                }
            }
            else if (!this.NothingFound) {
                if (!Newzgrabber.IsBatch) {
                    TransferPanel.downloading.setValueAt("Done!", this.TableIndex, 1);
                    TransferPanel.downloading.setValueAt(FullFilename, this.TableIndex, 3);
                    if (Newzgrabber.verbose) {
                        System.out.println("There are currently " + TransferPanel.CurrentDownloads + " downloads now");
                    }
                    TransferPanel.incrementFinished();
                }
                else {
                    final StringBuffer sbids = new StringBuffer("(");
                    for (int idindex = 0; idindex < this.ids.length; ++idindex) {
                        sbids.append(this.ids[idindex] + " ");
                    }
                    sbids.replace(sbids.length() - 1, sbids.length(), ")");
                    final String idstring = sbids.toString();
                    if (this.EncType == 1) {
                        System.out.println("UU Decoded " + FullFilename + " " + idstring);
                    }
                    else if (this.EncType == 2) {
                        System.out.println("Base64 Decoded " + FullFilename + " " + idstring);
                    }
                    else if (this.EncType == 3) {
                        System.out.println("yEnc Decoded " + FullFilename + " " + idstring);
                    }
                    else {
                        System.out.println("I don't understand encoding of " + idstring);
                    }
                }
            }
            else {
                if (Newzgrabber.verbose) {
                    System.out.println("Skipping because no file could be found");
                }
                if (!Newzgrabber.IsBatch) {
                    TransferPanel.downloading.setValueAt("Nothing found", this.TableIndex, 1);
                }
            }
        }
        catch (Exception downloaderE) {
            if (!Newzgrabber.IsBatch) {
                TransferPanel.downloading.setValueAt(downloaderE.getMessage(), this.TableIndex, 1);
            }
            else {
                System.out.println(downloaderE.getMessage());
            }
        }
    }
    
    private boolean findFile() {
        if (Newzgrabber.verbose) {
            System.out.println("Inside of the findFile method");
        }
        try {
            int line_number = 1;
            String NextLine = null;
            while ((NextLine = this.br.readLine()) != null) {
                ++this.checkcount;
                if (Newzgrabber.verbose) {
                    System.out.println("Reading line number " + line_number + "\n" + NextLine);
                }
                if (line_number == 1 && NextLine.trim().toLowerCase().startsWith("423 no")) {
                    if (Newzgrabber.verbose) {
                        System.out.println("Returning because no article");
                    }
                    return false;
                }
                ++line_number;
                if (NextLine.equals(".")) {
                    break;
                }
                this.br.mark(Downloader.MARKLIMIT);
                if (NextLine.startsWith("begin ")) {
                    final StringTokenizer st = new StringTokenizer(NextLine);
                    if (st.countTokens() > 2) {
                        st.nextToken();
                        try {
                            final int mode = Integer.parseInt(st.nextToken());
                        }
                        catch (Exception ie) {
                            continue;
                        }
                        this.EncType = 1;
                        final StringBuffer sb = new StringBuffer("");
                        while (st.hasMoreTokens()) {
                            sb.append(st.nextToken() + " ");
                        }
                        this.Filename = sb.toString().trim();
                        if (this.Filename != null) {
                            this.fixFilename();
                        }
                        this.br.reset();
                        return true;
                    }
                    continue;
                }
                else if (NextLine.toLowerCase().startsWith("=ybegin")) {
                    if (Newzgrabber.verbose) {
                        System.out.println("Found ybegin line in\n" + NextLine);
                    }
                    final int nameindex = NextLine.toLowerCase().indexOf("name=");
                    if (Newzgrabber.verbose) {
                        System.out.println("The name index is at " + nameindex);
                    }
                    if (nameindex != -1) {
                        this.Filename = NextLine.substring(nameindex + 5, NextLine.length());
                        this.EncType = 3;
                        this.br.reset();
                        return true;
                    }
                    continue;
                }
                else if (NextLine.toLowerCase().startsWith("content-type:")) {
                    if (NextLine.indexOf("name=") == -1) {
                        continue;
                    }
                    this.Filename = NextLine.substring(NextLine.indexOf("=") + 1, NextLine.length());
                    if (this.Filename == null) {
                        continue;
                    }
                    this.fixFilename();
                }
                else {
                    if (!NextLine.toLowerCase().trim().startsWith("content-transfer-encoding:")) {
                        continue;
                    }
                    final StringTokenizer st = new StringTokenizer(NextLine, ": ");
                    st.nextToken();
                    if (!st.nextToken().toLowerCase().trim().equals("base64")) {
                        continue;
                    }
                    if (Newzgrabber.verbose) {
                        System.out.println("This looks like it's base64 encoded");
                    }
                    if (this.Filename != null) {
                        this.EncType = 2;
                        return true;
                    }
                    String FileLine = null;
                    while ((FileLine = this.br.readLine()) != null) {
                        ++this.checkcount;
                        if (this.checkcount > Downloader.TIMEOUTCOUNT) {
                            break;
                        }
                        if (FileLine.equals(".")) {
                            break;
                        }
                        if (Newzgrabber.verbose) {
                            System.out.println("Trying to get the filename from\n" + FileLine);
                        }
                        if (FileLine.indexOf("filename=") != -1) {
                            this.Filename = FileLine.substring(FileLine.indexOf("=") + 1, FileLine.length());
                            if (Newzgrabber.verbose) {
                                System.out.println("The NON-fixed filename is '" + this.Filename + "'");
                            }
                            if (this.Filename != null) {
                                this.fixFilename();
                            }
                            if (Newzgrabber.verbose) {
                                System.out.println("The filename looks like '" + this.Filename + "'");
                            }
                            this.br.reset();
                            this.EncType = 2;
                            return true;
                        }
                        if (!Newzgrabber.verbose) {
                            continue;
                        }
                        System.out.println("Could not find the filename!");
                    }
                }
            }
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    private void fixFilename() {
        try {
            final StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < this.Filename.length(); ++i) {
                final char c = this.Filename.charAt(i);
                if (Character.isLetterOrDigit(c)) {
                    sb.append(c);
                }
                else if (Character.isSpaceChar(c)) {
                    sb.append(" ");
                }
                else if (c == '.' || c == '_' || c == '-') {
                    sb.append(c);
                }
            }
            this.Filename = sb.toString().trim();
        }
        catch (Exception ex) {}
    }
    
    static {
        Downloader.MARKLIMIT = 1000;
        Downloader.TIMEOUTCOUNT = 100;
    }
}
