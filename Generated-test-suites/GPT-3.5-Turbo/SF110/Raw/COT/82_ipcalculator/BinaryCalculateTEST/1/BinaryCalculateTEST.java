import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class BinaryCalculateTest {

    private BinaryCalculate binaryCalculate;

    @Before
    public void setUp() {
        binaryCalculate = new BinaryCalculate();
    }

    @Test
    public void testAddBinary() {
        assertEquals("10", binaryCalculate.addBinary("1", "1"));
        assertEquals("11110", binaryCalculate.addBinary("1111", "1111"));
        assertEquals("10000", binaryCalculate.addBinary("1111", "1"));
        assertEquals("111", binaryCalculate.addBinary("101", "10"));
    }

    @Test
    public void testSubBinary() {
        assertEquals("1", binaryCalculate.subBinary("10", "1"));
        assertEquals("1111", binaryCalculate.subBinary("11110", "1"));
        assertEquals("1111", binaryCalculate.subBinary("10000", "1"));
        assertEquals("11", binaryCalculate.subBinary("111", "10"));
    }

    @Test
    public void testIPCalculate() {
        String expectedOutput = "Total Range: 192.168.0.0 -- 192.168.0.255\n" +
                "Usable Range: 192.168.0.1 -- 192.168.0.254\n" +
                "\n" +
                "Total usable IP Addresses : 254\n" +
                "Subnet: 192.168.0.0\n" +
                "Binary Subnet: 11000000.10101000.00000000.00000000\n" +
                "Broadcast Address: 192.168.0.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000";

        assertEquals(expectedOutput, binaryCalculate.IPCalculate("11000000101010000000000000000000", "24"));
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        // Add test case for prefixInPrefixCalculate method
    }

    @Test
    public void testCheckIPType() {
        // Add test case for checkIPType method
    }

    @Test
    public void testConvert() {
        // Add test case for convert method
    }

    @Test
    public void testConvertToBinary() {
        // Add test case for convertToBinary method
    }

    @Test
    public void testGetStartIP() {
        // Add test case for getStartIP method
    }

}

