import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.ChartDataModelConstraints;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.font.FontRenderContext;
import java.text.DecimalFormat;

public class CoordSystemUtilitiesTest {
    private static CoordSystemUtilities utilities;
    private static CoordSystem c;
    private static ChartDataModelConstraints constraints;
    private static ChartDataModelConstraints constraints2;
    private static ChartDataModel model;
    private static Graphics2D g;
    
    @BeforeClass
    public static void setUp() {
        c = new CoordSystem();
        constraints = new ChartDataModelConstraints();
        constraints2 = new ChartDataModelConstraints();
        model = new ChartDataModel();
        utilities = new CoordSystemUtilities(c, constraints, constraints2, model);
    }
    
    @Test
    public void testComputeLeftMargin() {
        int leftMargin = utilities.computeLeftMargin();
        assertTrue(leftMargin >= 20);
    }
    
    @Test
    public void testComputeRightMargin() {
        int rightMargin = utilities.computeRightMargin();
        assertTrue(rightMargin >= 15);
    }
    
    @Test
    public void testComputeTopMargin() {
        int topMargin = utilities.computeTopMargin();
        assertTrue(topMargin >= 15);
    }
    
    @Test
    public void testComputeBottomMargin() {
        int bottomMargin = utilities.computeBottomMargin();
        assertTrue(bottomMargin >= 20);
    }
    
    @Test
    public void testComputeXAxisLabelHeight() {
        int xAxisLabelHeight = utilities.computeXAxisLabelHeight();
        assertTrue(xAxisLabelHeight >= 0);
    }
    
    @Test
    public void testComputeYAxisLabelWidth() {
        int yAxisLabelWidth = utilities.computeYAxisLabelWidth();
        assertTrue(yAxisLabelWidth >= 0);
    }
    
    @Test
    public void testDrawNumericalXAxisTicks() {
        utilities.drawNumericalXAxisTicks(g);
    }
    
    @Test
    public void testDrawXAxisTicks() {
        utilities.drawXAxisTicks(g);
    }
    
    @Test
    public void testDrawYAxisTicks() {
        utilities.drawYAxisTicks(g);
    }
    
    @Test
    public void testGetXAxisLine2D() {
        Line2D xaxis = utilities.getXAxisLine2D();
        assertNotNull(xaxis);
    }
    
    @Test
    public void testGetYAxisLine2D() {
        Line2D yaxis = utilities.getYAxisLine2D();
        assertNotNull(yaxis);
    }
    
    @Test
    public void testGetSecondYAxisLine2D() {
        Line2D secondYaxis = utilities.getSecondYAxisLine2D();
        assertNotNull(secondYaxis);
    }
}