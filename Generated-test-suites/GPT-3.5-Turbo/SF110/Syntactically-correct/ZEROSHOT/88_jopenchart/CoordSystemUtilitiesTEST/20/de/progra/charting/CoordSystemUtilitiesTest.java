import org.junit.*;
import static org.junit.Assert.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        // Test when xmin is less than or equal to zero and xmax is greater than zero
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        coord.setYAxisUnit("Unit");
        coord.setFont(new Font("SansSerif", Font.PLAIN, 12));

        CoordSystemUtilities utilities = new CoordSystemUtilities(coord, constraints, null, null);
        int result = utilities.computeLeftMargin();

        assertEquals("Failed to compute left margin correctly", 38, result);
    }

    @Test
    public void testComputeRightMargin() {
        // Test when x-axis unit is "Unit"
        CoordSystem coord = new CoordSystem();
        coord.setXAxisUnit("Unit");
        coord.setFont(new Font("SansSerif", Font.PLAIN, 12));

        CoordSystemUtilities utilities = new CoordSystemUtilities(coord, null, null, null);
        int result = utilities.computeRightMargin();

        assertEquals("Failed to compute right margin correctly", 38, result);
    }

    @Test
    public void testComputeTopMargin() {
        // Test when y-axis unit is "Unit"
        CoordSystem coord = new CoordSystem();
        coord.setYAxisUnit("Unit");
        coord.setFont(new Font("SansSerif", Font.PLAIN, 12));

        CoordSystemUtilities utilities = new CoordSystemUtilities(coord, null, null, null);
        int result = utilities.computeTopMargin();

        assertEquals("Failed to compute top margin correctly", 38, result);
    }

    @Test
    public void testComputeBottomMargin() {
        // Test when ymin is less than or equal to zero and ymax is greater than zero
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(0.0);
        constraints.setMaximumValue(10.0);
        coord.setXAxisUnit("Unit");
        coord.setFont(new Font("SansSerif", Font.PLAIN, 12));

        CoordSystemUtilities utilities = new CoordSystemUtilities(coord, constraints, null, null);
        int result = utilities.computeBottomMargin();

        assertEquals("Failed to compute bottom margin correctly", 28, result);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(10.0);
        coord.setXAxisUnit("Unit");
        coord.setFont(new Font("SansSerif", Font.PLAIN, 12));

        CoordSystemUtilities utilities = new CoordSystemUtilities(coord, constraints, null, null);
        int result = utilities.computeXAxisLabelHeight();

        assertEquals("Failed to compute x-axis label height correctly", 16, result);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(0.0);
        constraints.setMaximumValue(10.0);
        coord.setYAxisUnit("Unit");
        coord.setFont(new Font("SansSerif", Font.PLAIN, 12));

        CoordSystemUtilities utilities = new CoordSystemUtilities(coord, constraints, null, null);
        int result = utilities.computeYAxisLabelWidth();

        assertEquals("Failed to compute y-axis label width correctly", 14, result);
    }

    // Additional tests for other methods in CoordSystemUtilities class can be added here

}