package weka.core;

import javax.swing.*;
import java.awt.*;

public class Memory implements RevisionHandler
{
    protected static boolean m_Enabled;
    protected boolean m_UseGUI;
    protected static long m_Initial;
    protected long m_Total;
    protected long m_Max;
    protected Runtime m_Runtime;
    
    public Memory() {
        this(false);
    }
    
    public Memory(final boolean useGUI) {
        this.m_UseGUI = false;
        this.m_UseGUI = useGUI;
        this.m_Runtime = Runtime.getRuntime();
        this.m_Max = this.m_Runtime.maxMemory();
        this.m_Total = this.m_Runtime.totalMemory();
    }
    
    public boolean isEnabled() {
        return Memory.m_Enabled;
    }
    
    public void setEnabled(final boolean value) {
        Memory.m_Enabled = value;
    }
    
    public boolean getUseGUI() {
        return this.m_UseGUI;
    }
    
    public long getInitial() {
        return Memory.m_Initial;
    }
    
    public long getCurrent() {
        this.m_Runtime = Runtime.getRuntime();
        return this.m_Total = this.m_Runtime.totalMemory();
    }
    
    public long getMax() {
        return this.m_Max;
    }
    
    public boolean isOutOfMemory() {
        return this.isEnabled() && this.getMax() - this.getCurrent() < this.getInitial() + 200000L;
    }
    
    public static double toMegaByte(final long bytes) {
        return bytes / 1048576.0;
    }
    
    public void showOutOfMemory() {
        if (!this.isEnabled()) {
            return;
        }
        System.gc();
        final String msg = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   " + Utils.doubleToString(toMegaByte(Memory.m_Initial), 1) + "MB\n" + "- total memory used:  " + Utils.doubleToString(toMegaByte(this.m_Total), 1) + "MB\n" + "- max. memory avail.: " + Utils.doubleToString(toMegaByte(this.m_Max), 1) + "MB\n" + "\n" + "Note:\n" + "The Java heap size can be specified with the -Xmx option.\n" + "E.g., to use 128MB as heap size, the command line looks like this:\n" + "   java -Xmx128m -classpath ...\n" + "This does NOT work in the SimpleCLI, the java command refers\n" + "to the one with which Weka is started.";
        System.err.println(msg);
        if (this.getUseGUI()) {
            JOptionPane.showMessageDialog(null, msg, "OutOfMemory", 2);
        }
    }
    
    public void stopThreads() {
        Thread[] thGroup = new Thread[Thread.activeCount()];
        Thread.enumerate(thGroup);
        for (int i = 0; i < thGroup.length; ++i) {
            final Thread t = thGroup[i];
            if (t != null && t != Thread.currentThread()) {
                if (t.getName().startsWith("Thread")) {
                    t.stop();
                }
                else if (t.getName().startsWith("AWT-EventQueue")) {
                    t.stop();
                }
            }
        }
        thGroup = null;
        System.gc();
    }
    
    @Override
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 8034 $");
    }
    
    public static void main(final String[] args) {
        final Memory mem = new Memory();
        System.out.println("Initial memory: " + Utils.doubleToString(toMegaByte(mem.getInitial()), 1) + "MB" + " (" + mem.getInitial() + ")");
        System.out.println("Max memory: " + Utils.doubleToString(toMegaByte(mem.getMax()), 1) + "MB" + " (" + mem.getMax() + ")");
    }
    
    static {
        Memory.m_Enabled = true;
        Memory.m_Initial = Runtime.getRuntime().totalMemory();
    }
}