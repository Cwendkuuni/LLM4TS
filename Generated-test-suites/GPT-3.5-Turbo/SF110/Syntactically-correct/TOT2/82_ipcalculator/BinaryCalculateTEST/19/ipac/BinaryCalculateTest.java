package ipac;
import org.junit.*;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    private BinaryCalculate binaryCalculate;

    @Before
    public void setUp() {
        binaryCalculate = new BinaryCalculate();
    }

    @Test
    public void testAddBinary() {
        assertEquals("10", binaryCalculate.addBinary("1", "1"));
    }

    @Test
    public void testSubBinary() {
        assertEquals("10", binaryCalculate.subBinary("11", "1"));
    }

    @Test
    public void testIPCalculate() {
        String expectedOutput = "Total Range: 192.168.1.0 -- ";
        expectedOutput += "Usable Range: 192.168.1.1 -- ";
        expectedOutput += "\nTotal usable IP Addresses : 253";
        expectedOutput += "\nSubnet: 192.168.1.0";
        expectedOutput += "\nBinary Subnet: 11000000010101000000000100000000";
        expectedOutput += "\nBroadcast Address: 192.168.1.31";
        expectedOutput += "\nPrefix: /27";
        expectedOutput += "\nNetmask: 255.255.255.224";
        expectedOutput += "\nBinary Netmask: 11111111.11111111.11111111.11100000";
        
        assertEquals(expectedOutput, binaryCalculate.IPCalculate("192.168.1.0", "27"));
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        // test case for prefixInPrefixCalculate method
    }
}