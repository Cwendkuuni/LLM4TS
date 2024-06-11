package jahuwaldt.plot;

import java.io.*;
import java.text.*;
import java.awt.*;

public abstract class PlotAxis implements Cloneable, Serializable
{
    private static final boolean DEBUG = false;
    public static final int kTick = 5;
    public static final int kNoGrid = 0;
    public static final int kTickMarksOnly = 1;
    public static final int kMajorGrid = 2;
    protected boolean manualLB;
    protected boolean manualUB;
    protected boolean manualQuant;
    protected double xA;
    protected double xB;
    protected double aLB;
    protected double aUB;
    protected double quantum;
    protected int gridStyle;
    protected PlotAxisScale scale;
    protected NumberFormat nf;
    protected String label;
    
    protected PlotAxis() {
        this.xA = 1.0;
        this.xB = 1.0;
        this.aLB = Double.MAX_VALUE;
        this.aUB = -1.7976931348623157E308;
        this.gridStyle = 1;
        this.scale = new LinearAxisScale();
        this.nf = NumberFormat.getInstance();
    }
    
    protected PlotAxis(final String label, final PlotAxisScale scale, final NumberFormat nf, final int gridStyle) {
        this.xA = 1.0;
        this.xB = 1.0;
        this.aLB = Double.MAX_VALUE;
        this.aUB = -1.7976931348623157E308;
        this.gridStyle = 1;
        this.scale = new LinearAxisScale();
        this.nf = NumberFormat.getInstance();
        this.label = label;
        if (scale != null) {
            this.scale = scale;
        }
        if (nf != null) {
            this.nf = nf;
        }
        this.gridStyle = gridStyle;
    }
    
    public void setLabel(final String newLabel) {
        this.label = newLabel;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void setScale(final PlotAxisScale scale) {
        if (scale == null) {
            this.scale = new LinearAxisScale();
        }
        else {
            this.scale = scale;
        }
    }
    
    public PlotAxisScale getScale() {
        return this.scale;
    }
    
    public void setNumberFormat(final NumberFormat format) {
        if (format == null) {
            this.nf = NumberFormat.getInstance();
        }
        else {
            this.nf = format;
        }
    }
    
    public NumberFormat getNumberFormat() {
        return this.nf;
    }
    
    public void setGridStyle(final int style) {
        this.gridStyle = style;
    }
    
    public int getGridStyle() {
        return this.gridStyle;
    }
    
    public void manualLowerBounds(final boolean flag) {
        this.manualLB = flag;
    }
    
    public void setLowerBounds(final double value) {
        this.aLB = value;
    }
    
    public double getLowerBounds() {
        return this.aLB;
    }
    
    public String lowerBoundsAsString() {
        return this.nf.format((float)this.aLB);
    }
    
    public void manualUpperBounds(final boolean flag) {
        this.manualUB = flag;
    }
    
    public void setUpperBounds(final double value) {
        this.aUB = value;
    }
    
    public double getUpperBounds() {
        return this.aUB;
    }
    
    public String upperBoundsAsString() {
        return this.nf.format((float)this.aUB);
    }
    
    public void manualTickStepSize(final boolean flag) {
        this.manualQuant = flag;
    }
    
    public void setTickStepSize(final double value) {
        this.quantum = value;
    }
    
    public double getTickStepSize() {
        return this.quantum;
    }
    
    public abstract void setAxisBounds(final PlotRunList p0);
    
    public void setSize(final Rectangle plotFrame) {
        this.setAxisTickLimits();
        this.resizeAxis(plotFrame);
    }
    
    protected void setAxisTickLimits() {
        if (this.manualQuant) {
            return;
        }
        boolean lbf = this.manualLB;
        boolean ubf = this.manualUB;
        double sign = 1.0;
        double lb = this.aLB;
        double ub = this.aUB;
        final double delta = this.aUB - this.aLB;
        if (delta < 0.0) {
            sign = -1.0;
            final double temp = lb;
            lb = ub;
            ub = temp;
            final boolean btemp = lbf;
            lbf = ubf;
            ubf = btemp;
        }
        final AxisLimitData limData = this.scale.findGoodLimits(lb, ub);
        if (!this.manualLB) {
            if (sign > 0.0) {
                this.aLB = limData.lb;
            }
            else {
                this.aLB = limData.ub;
            }
        }
        if (!this.manualUB) {
            if (sign > 0.0) {
                this.aUB = limData.ub;
            }
            else {
                this.aUB = limData.lb;
            }
        }
        this.quantum = sign * limData.quantum;
    }
    
    protected abstract void resizeAxis(final Rectangle p0);
    
    public abstract void draw(final Graphics p0, final Component p1, final Rectangle p2);
    
    public int scaleCoord(final double x) {
        return (int)(this.xA * this.scale.func(x) + this.xB);
    }
    
    public Object clone() {
        PlotAxis newObject = null;
        try {
            newObject = (PlotAxis)super.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newObject;
    }
}
