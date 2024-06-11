package de.progra.charting;

import de.progra.charting.model.*;
import java.text.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.*;

public class CoordSystemUtilities
{
    protected final int marginOffset = 6;
    protected CoordSystem c;
    protected ChartDataModelConstraints constraints;
    protected ChartDataModelConstraints constraints2;
    protected ChartDataModel model;
    
    public CoordSystemUtilities(final CoordSystem coord, final ChartDataModelConstraints constraints, final ChartDataModelConstraints constraints2, final ChartDataModel model) {
        this.c = coord;
        this.constraints = constraints;
        this.constraints2 = constraints2;
        this.model = model;
    }
    
    public int computeLeftMargin() {
        double xmin = this.constraints.getMinimumColumnValue();
        double xmax = this.constraints.getMaximumColumnValue();
        if (xmin <= 0.0 && xmax > 0.0) {
            xmin = Math.abs(xmin);
            xmax = Math.abs(xmax);
            final TextLayout layout = new TextLayout(this.c.getYAxisUnit(), this.c.getFont(), this.c.getFontRenderContext());
            int maxlmargin = this.computeYAxisLabelWidth() + 6;
            maxlmargin = Math.max(maxlmargin, (int)layout.getBounds().getWidth() + 6);
            int margin = (int)(maxlmargin - xmin / (xmin + xmax) * (this.c.getBounds().getWidth() - this.c.getRightMargin()));
            margin += 5;
            final int n = margin;
            this.c.getClass();
            if (n < 20) {
                this.c.getClass();
                margin = 20;
            }
            return margin;
        }
        this.c.getClass();
        return 20;
    }
    
    public int computeRightMargin() {
        final TextLayout layout = new TextLayout(this.c.getXAxisUnit(), this.c.getFont(), this.c.getFontRenderContext());
        final double width = layout.getBounds().getWidth();
        this.c.getClass();
        final int n = (int)(width + 15.0 / 3.0);
        this.c.getClass();
        return Math.max(n, 15);
    }
    
    public int computeTopMargin() {
        final TextLayout layout = new TextLayout(this.c.getYAxisUnit(), this.c.getFont(), this.c.getFontRenderContext());
        final double height = layout.getBounds().getHeight();
        this.c.getClass();
        final int n = (int)(height + 15.0 / 3.0 + layout.getDescent());
        this.c.getClass();
        return Math.max(n, 15);
    }
    
    public int computeBottomMargin() {
        double ymin = this.constraints.getMinimumValue().doubleValue();
        double ymax = this.constraints.getMaximumValue().doubleValue();
        if (ymin <= 0.0 && ymax > 0.0) {
            ymin = Math.abs(ymin);
            ymax = Math.abs(ymax);
            final TextLayout layout = new TextLayout(this.c.getXAxisUnit(), this.c.getFont(), this.c.getFontRenderContext());
            int maxbmargin = this.computeXAxisLabelHeight() + 6;
            maxbmargin = Math.max(maxbmargin, (int)layout.getBounds().getHeight() + 6);
            int margin = (int)(maxbmargin - ymin / (ymin + ymax) * (this.c.getBounds().getHeight() - this.c.getTopMargin()));
            margin += 10;
            final int n = margin;
            this.c.getClass();
            if (n < 20) {
                this.c.getClass();
                margin = 20;
            }
            return margin;
        }
        this.c.getClass();
        return 20;
    }
    
    public int computeXAxisLabelHeight() {
        final double min = this.constraints.getMinimumColumnValue();
        final double max = this.constraints.getMaximumColumnValue();
        final double tick = ChartUtilities.calculateTickSpacing(min, max);
        double ypt = 0.0;
        int height = 0;
        if (this.constraints.getMinimumValue().doubleValue() > 0.0) {
            ypt = this.constraints.getMinimumValue().doubleValue();
        }
        else if (this.constraints.getMaximumValue().doubleValue() < 0.0) {
            ypt = this.constraints.getMaximumValue().doubleValue();
        }
        boolean paint = false;
        final DecimalFormat df = this.c.getXDecimalFormat();
        final FontRenderContext frc = this.c.getFontRenderContext();
        final Font f = this.c.getFont();
        for (double d = min; d <= max; d += tick) {
            if (paint) {
                final String sb = df.format(d);
                final Rectangle2D r = f.getStringBounds(sb, frc);
                height = Math.max(height, (int)r.getHeight());
            }
            paint = !paint;
        }
        return height;
    }
    
    public int computeYAxisLabelWidth() {
        final double min = this.constraints.getMinimumValue().doubleValue();
        final double max = this.constraints.getMaximumValue().doubleValue();
        final double tick = ChartUtilities.calculateTickSpacing(min, max);
        double xpt = 0.0;
        int width = 0;
        if (this.constraints.getMinimumColumnValue() > 0.0) {
            xpt = this.constraints.getMinimumColumnValue();
        }
        else if (this.constraints.getMaximumColumnValue() < 0.0 && this.c.getSecondYAxis() != null) {
            xpt = this.constraints.getMaximumColumnValue();
        }
        boolean paint = false;
        final DecimalFormat df = this.c.getYDecimalFormat();
        final FontRenderContext frc = this.c.getFontRenderContext();
        final Font f = this.c.getFont();
        for (double d = min; d <= max; d += tick) {
            if (paint) {
                final String sb = df.format(d);
                final Rectangle2D r = f.getStringBounds(sb, frc);
                width = Math.max((int)r.getWidth(), width);
            }
            paint = !paint;
        }
        return width;
    }
    
    public void drawNumericalXAxisTicks(final Graphics2D g) {
        final AffineTransform at = this.c.getTransform(0);
        final double min = this.constraints.getMinimumColumnValue();
        final double max = this.constraints.getMaximumColumnValue();
        final double tick = ChartUtilities.calculateTickSpacing(min, max);
        double ypt = 0.0;
        if (this.constraints.getMinimumValue().doubleValue() > 0.0) {
            ypt = this.constraints.getMinimumValue().doubleValue();
        }
        else if (this.constraints.getMaximumValue().doubleValue() < 0.0) {
            ypt = this.constraints.getMaximumValue().doubleValue();
        }
        final Point2D p = new Point2D.Double(0.0, 0.0);
        final Line2D ticks = new Line2D.Double(0.0, 0.0, 0.0, 0.0);
        final DecimalFormat df = this.c.getXDecimalFormat();
        final FontRenderContext frc = this.c.getFontRenderContext();
        final Font f = this.c.getFont();
        boolean paint = false;
        g.setFont(f);
        final boolean paintLabels = this.c.isPaintLabels();
        for (double d = min; d <= max; d += tick) {
            p.setLocation(d, ypt);
            final Point2D v = at.transform(p, null);
            ticks.setLine(v.getX(), v.getY() - 3.0, v.getX(), v.getY() + 3.0);
            g.draw(ticks);
            if (paint && paintLabels) {
                final String sb = df.format(d);
                final Rectangle2D r = f.getStringBounds(sb, frc);
                g.drawString(sb, (float)(v.getX() - r.getWidth() / 2.0), (float)(v.getY() + r.getHeight() + 6.0));
            }
            paint = !paint;
        }
    }
    
    public void drawXAxisTicks(final Graphics2D g) {
        final AffineTransform at = this.c.getTransform(0);
        final int min = (int)this.constraints.getMinimumColumnValue();
        final int max = (int)this.constraints.getMaximumColumnValue();
        final double tick = 1.0;
        double ypt = 0.0;
        if (this.constraints.getMinimumValue().doubleValue() > 0.0) {
            ypt = this.constraints.getMinimumValue().doubleValue();
        }
        else if (this.constraints.getMaximumValue().doubleValue() < 0.0) {
            ypt = this.constraints.getMaximumValue().doubleValue();
        }
        final Point2D p = new Point2D.Double(0.0, 0.0);
        Point2D v = null;
        Point2D oldv = null;
        final Line2D ticks = new Line2D.Double(0.0, 0.0, 0.0, 0.0);
        final DecimalFormat df = this.c.getXDecimalFormat();
        final FontRenderContext frc = this.c.getFontRenderContext();
        final Font f = this.c.getFont();
        final boolean paint = false;
        final boolean paintLabels = this.c.isPaintLabels();
        g.setFont(f);
        for (int i = min - 1; i < max; ++i) {
            p.setLocation(i + 1, ypt);
            v = at.transform(p, null);
            ticks.setLine(v.getX(), v.getY() - 3.0, v.getX(), v.getY() + 3.0);
            if (i + 1 < max) {
                g.draw(ticks);
            }
            if (oldv != null && paintLabels) {
                final String sb = (String)this.model.getColumnValueAt(i);
                final Rectangle2D r = f.getStringBounds(sb, frc);
                g.drawString(sb, (float)(oldv.getX() + (v.getX() - oldv.getX()) / 2.0 - r.getWidth() / 2.0), (float)(v.getY() + r.getHeight() + 6.0));
            }
            oldv = v;
        }
    }
    
    public void drawYAxisTicks(final Graphics2D g) {
        final AffineTransform at = this.c.getTransform(0);
        final double min = this.constraints.getMinimumValue().doubleValue();
        final double max = this.constraints.getMaximumValue().doubleValue();
        final double tick = ChartUtilities.calculateTickSpacing(min, max);
        double xpt = 0.0;
        if (this.constraints.getMinimumColumnValue() > 0.0) {
            xpt = this.constraints.getMinimumColumnValue();
        }
        else if (this.constraints.getMaximumColumnValue() < 0.0 && this.c.getSecondYAxis() != null) {
            xpt = this.constraints.getMaximumColumnValue();
        }
        final Point2D p = new Point2D.Double(0.0, 0.0);
        final Line2D ticks = new Line2D.Double(0.0, 0.0, 0.0, 0.0);
        boolean paint = false;
        final DecimalFormat df = this.c.getYDecimalFormat();
        final FontRenderContext frc = this.c.getFontRenderContext();
        final Font f = this.c.getFont();
        final Color backupColor = g.getColor();
        g.setFont(f);
        final boolean paintLabels = this.c.isPaintLabels();
        for (double d = min; d <= max; d += tick) {
            p.setLocation(xpt, d);
            final Point2D v = at.transform(p, null);
            ticks.setLine(v.getX() - 3.0, v.getY(), v.getX() + 3.0, v.getY());
            g.draw(ticks);
            if (d != min && !this.c.isPaintOnlyTick()) {
                final Line2D xax = this.getXAxisLine2D();
                ticks.setLine(v.getX() + 3.0, v.getY(), xax.getX2(), v.getY());
                g.setColor(Color.lightGray);
                g.draw(ticks);
                g.setColor(backupColor);
            }
            if (paintLabels && (paint || !this.c.isPaintAltTick())) {
                final String sb = df.format(d);
                final Rectangle2D r = f.getStringBounds(sb, frc);
                g.drawString(sb, (float)(v.getX() - r.getWidth() - 6.0), (float)(v.getY() + r.getHeight() / 2.0));
            }
            paint = !paint;
        }
    }
    
    public Line2D getXAxisLine2D() {
        double ypt = 0.0;
        if (this.constraints.getMinimumValue().doubleValue() > 0.0) {
            ypt = this.constraints.getMinimumValue().doubleValue();
        }
        else if (this.constraints.getMaximumValue().doubleValue() < 0.0) {
            ypt = this.constraints.getMaximumValue().doubleValue();
        }
        final AffineTransform at = this.c.getTransform(0);
        final Point2D l = at.transform(new Point2D.Double(this.constraints.getMinimumColumnValue(), ypt), null);
        final Point2D r = at.transform(new Point2D.Double(this.constraints.getMaximumColumnValue(), ypt), null);
        return new Line2D.Double(l, r);
    }
    
    public Line2D getYAxisLine2D() {
        double xpt = 0.0;
        if (this.constraints.getMinimumColumnValue() > 0.0) {
            xpt = this.constraints.getMinimumColumnValue();
        }
        else if (this.constraints.getMaximumColumnValue() < 0.0 && this.c.getSecondYAxis() != null) {
            xpt = this.constraints.getMaximumColumnValue();
        }
        final AffineTransform at = this.c.getTransform(0);
        final Point2D o = at.transform(new Point2D.Double(xpt, this.constraints.getMaximumValue().doubleValue()), null);
        final Point2D u = at.transform(new Point2D.Double(xpt, this.constraints.getMinimumValue().doubleValue()), null);
        return new Line2D.Double(o, u);
    }
    
    public Line2D getSecondYAxisLine2D() {
        final double xpt = this.constraints2.getMaximumColumnValue();
        final AffineTransform at = this.c.getTransform(1);
        final Point2D o = at.transform(new Point2D.Double(xpt, this.constraints2.getMaximumValue().doubleValue()), null);
        final Point2D u = at.transform(new Point2D.Double(xpt, this.constraints2.getMinimumValue().doubleValue()), null);
        return new Line2D.Double(o, u);
    }
}
