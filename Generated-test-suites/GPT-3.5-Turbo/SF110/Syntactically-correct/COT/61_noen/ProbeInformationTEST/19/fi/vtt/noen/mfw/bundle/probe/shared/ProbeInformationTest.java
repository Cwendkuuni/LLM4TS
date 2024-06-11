import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    private ProbeInformation probeInformation;

    @Test
    public void testGetTargetType() {
        String targetType = "Test Target Type";
        probeInformation = new ProbeInformation("Test Target Name", targetType,
                "Test bmClass", "Test bmName", "Test bmDescription", "Test probeDescription",
                10, "Test xmlRpcUrl");

        assertEquals(targetType, probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        String bmName = "Test Bm Name";
        probeInformation = new ProbeInformation("Test Target Name", "Test Target Type",
                "Test bmClass", bmName, "Test bmDescription", "Test probeDescription",
                10, "Test xmlRpcUrl");

        assertEquals(bmName, probeInformation.getBmName());
    }

    @Test
    public void testGetProbeName() {
        String probeName = "Test Probe Name";
        probeInformation = new ProbeInformation("Test Target Name", "Test Target Type",
                "Test bmClass", "Test bmName", "Test bmDescription", probeName,
                10, "Test xmlRpcUrl");

        assertEquals(probeName, probeInformation.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        String targetName = "Test Target Name";
        probeInformation = new ProbeInformation(targetName, "Test Target Type",
                "Test bmClass", "Test bmName", "Test bmDescription", "Test probeDescription",
                10, "Test xmlRpcUrl");

        assertEquals(targetName, probeInformation.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        String bmClass = "Test Bm Class";
        probeInformation = new ProbeInformation("Test Target Name", "Test Target Type",
                bmClass, "Test bmName", "Test bmDescription", "Test probeDescription",
                10, "Test xmlRpcUrl");

        assertEquals(bmClass, probeInformation.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = "Test Bm Description";
        probeInformation = new ProbeInformation("Test Target Name", "Test Target Type",
                "Test bmClass", "Test bmName", bmDescription, "Test probeDescription",
                10, "Test xmlRpcUrl");

        assertEquals(bmDescription, probeInformation.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        int precision = 10;
        probeInformation = new ProbeInformation("Test Target Name", "Test Target Type",
                "Test bmClass", "Test bmName", "Test bmDescription", "Test probeDescription",
                precision, "Test xmlRpcUrl");

        assertEquals(precision, probeInformation.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "Test Target Type";
        String targetName = "Test Target Name";
        String bmClass = "Test bmClass";
        String bmName = "Test bmName";

        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        probeInformation = new ProbeInformation(targetName, targetType,
                bmClass, bmName, "Test bmDescription", "Test probeDescription",
                10, "Test xmlRpcUrl");

        assertEquals(expectedMeasureURI, probeInformation.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        String xmlRpcUrl = "Test xmlRpcUrl";
        probeInformation = new ProbeInformation("Test Target Name", "Test Target Type",
                "Test bmClass", "Test bmName", "Test bmDescription", "Test probeDescription",
                10, xmlRpcUrl);

        assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        String targetName = "Test Target Name";
        String targetType = "Test Target Type";
        String bmClass = "Test bmClass";
        String bmName = "Test bmName";
        String bmDescription = "Test bmDescription";
        String probeDescription = "Test probeDescription";
        int precision = 10;

        String expectedString = "ProbeInformation{targetName='" + targetName + '\'' + ", targetType='" + targetType + '\'' + ", bmClass='" + bmClass + '\'' + ", bmName='" + bmName + '\'' + ", bmDescription='" + bmDescription + '\'' + ", probeDescription='" + probeDescription + '\'' + ", precision=" + precision + '}';
        probeInformation = new ProbeInformation(targetName, targetType,
                bmClass, bmName, bmDescription, probeDescription,
                precision, "Test xmlRpcUrl");

        assertEquals(expectedString, probeInformation.toString());
    }

    @Test
    public void testEquals() {
        String targetName = "Test Target Name";
        String targetType = "Test Target Type";
        String bmClass = "Test bmClass";
        String bmName = "Test bmName";
        String bmDescription = "Test bmDescription";
        String probeDescription = "Test probeDescription";
        int precision = 10;

        ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType,
                bmClass, bmName, bmDescription, probeDescription,
                precision, "Test xmlRpcUrl");

        ProbeInformation probeInformation2 = new ProbeInformation("Different Target Name", targetType,
                bmClass, bmName, bmDescription, probeDescription,
                precision, "Test xmlRpcUrl");

        assertTrue(probeInformation1.equals(probeInformation1));
        assertFalse(probeInformation1.equals(null));
        assertFalse(probeInformation1.equals("Test String"));
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testHashCode() {
        String targetName = "Test Target Name";
        String targetType = "Test Target Type";
        String bmClass = "Test bmClass";
        String bmName = "Test bmName";
        String bmDescription = "Test bmDescription";
        String probeDescription = "Test probeDescription";
        int precision = 10;

        ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType,
                bmClass, bmName, bmDescription, probeDescription,
                precision, "Test xmlRpcUrl");

        ProbeInformation probeInformation2 = new ProbeInformation("Different Target Name", targetType,
                bmClass, bmName, bmDescription, probeDescription,
                precision, "Test xmlRpcUrl");

        assertEquals(probeInformation1.hashCode(), probeInformation1.hashCode());
        assertNotEquals(probeInformation1.hashCode(), probeInformation2.hashCode());
    }
}