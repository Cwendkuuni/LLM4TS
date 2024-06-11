package de.progra.charting;

import de.progra.charting.model.*;
import java.awt.*;
import de.progra.charting.render.*;
import java.util.*;

public class DefaultChart extends AbstractChart
{
    public static int LINEAR_X_LINEAR_Y;
    public static int NO_COORDINATE_SYSTEM;
    
    protected DefaultChart() {
    }
    
    public DefaultChart(final ChartDataModel model, final String title) {
        this();
        this.setChartDataModel(model);
        this.setRowColorModel(new RowColorModel(model));
        this.setLegend(new Legend(this.getRowColorModel()));
        this.setTitle(new Title(title));
    }
    
    public DefaultChart(final ChartDataModel model, final String title, final int coord) {
        this(model, title);
        if (coord == DefaultChart.LINEAR_X_LINEAR_Y) {
            this.setCoordSystem(new CoordSystem(model));
        }
    }
    
    public DefaultChart(final ChartDataModel model, final String title, final int coord, final String xaxis, final String yaxis) {
        this(model, title, coord);
        this.getCoordSystem().setXAxisUnit(xaxis);
        this.getCoordSystem().setYAxisUnit(yaxis);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return null;
    }
    
    @Override
    public void render(final Graphics2D g) {
        final int width = (int)this.getBounds().getWidth();
        final int height = (int)this.getBounds().getHeight();
        final Title t = this.getTitle();
        final Legend l = this.getLegend();
        final CoordSystem c = this.getCoordSystem();
        final Collection renderer = this.getChartRenderer().values();
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.black);
        int titleheight = 0;
        int legendwidth = 0;
        if (t != null) {
            final Dimension title = t.getPreferredSize();
            t.setBounds(new Rectangle(width / 2 - (int)(title.getWidth() / 2.0), 0, (int)title.getWidth(), (int)title.getHeight()));
            t.render(g);
            titleheight = (int)t.getBounds().getHeight();
        }
        if (l != null) {
            final Dimension legend = l.getPreferredSize();
            l.setBounds(new Rectangle((int)(width - legend.getWidth()), (int)(height / 2 - legend.getHeight() / 2.0 + titleheight), (int)legend.getWidth(), (int)legend.getHeight()));
            l.render(g);
            legendwidth = (int)l.getBounds().getWidth();
        }
        if (c != null) {
            c.setBounds(new Rectangle(0, titleheight, width - legendwidth, height - titleheight));
        }
        if (!renderer.isEmpty()) {
            for (final AbstractChartRenderer cr : renderer) {
                cr.setBounds(new Rectangle(0, titleheight, width - legendwidth, height - titleheight - 5));
                cr.render(g);
            }
        }
        if (c != null) {
            c.render(g);
        }
    }
    
    static {
        DefaultChart.LINEAR_X_LINEAR_Y = 0;
        DefaultChart.NO_COORDINATE_SYSTEM = 1;
    }
}
