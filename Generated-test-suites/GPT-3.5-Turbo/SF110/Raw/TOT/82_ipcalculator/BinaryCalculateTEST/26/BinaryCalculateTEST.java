package ipac;

import static org.junit.Assert.*;
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
        assertEquals("0", binaryCalculate.addBinary("0", "0"));
        assertEquals("1", binaryCalculate.addBinary("0", "1"));
        assertEquals("1", binaryCalculate.addBinary("1", "0"));
        assertEquals("10", binaryCalculate.addBinary("1", "1"));
    }
    
    @Test
    public void testSubBinary() {
        assertEquals("0", binaryCalculate.subBinary("0", "0"));
        assertEquals("0", binaryCalculate.subBinary("1", "1"));
        assertEquals("0", binaryCalculate.subBinary("10", "10"));
        assertEquals("10", binaryCalculate.subBinary("10", "1"));
    }
    
    @Test
    public void testIPCalculate() {
        String IP = "192.168.0.1";
        String IPPrefix = "24";
        String expectedOutput = "Total Range: 192.168.0.1 -- 192.168.0.1\n" +
                                "Usable Range: 192.168.0.1 -- 192.168.0.1\n" +
                                "\n" +
                                "Total usable IP Addresses : 1\n" +
                                "Subnet: 192.168.0.1\n" +
                                "Binary Subnet: 11000000.10101000.00000000.00000001\n" +
                                "Broadcast Address: \n" +
                                "Prefix: \n" +
                                "Netmask: \n" +
                                "Binary Netmask: \n";
        assertEquals(expectedOutput, binaryCalculate.IPCalculate(IP, IPPrefix));
    }
    
    @Test
    public void testPrefixInPrefixCalculate() {
        String IP = "192.168.0.1";
        String IPPrefix = "24";
        int noPrefix = 3;
        String endPrefix = "26";
        String[][] expectedOutput = {
            {"192.168.0.1 -- /26", "192.168.0.1 -- "},
            {"192.168.0.1 -- /26", "192.168.0.1 -- "},
            {"192.168.0.1 -- /26", "192.168.0.1 -- "},
            {"Total usable IP Addresses :", "6 "},
            {"Subnet:", "192.168.0.1"},
            {"Binary Subnet:", "11000000.10101000.00000000.00000001"},
            {"Broadcast Address:", ""},
            {"Prefix:", "26"},
            {"Netmask", ""},
            {"Binary Netmask:", ""}
        };
        
        binaryCalculate.prefixInPrefixCalculate(IP, IPPrefix, noPrefix, endPrefix);
        assertEquals(expectedOutput, binaryCalculate.prefixInPrefixCalculate(IP, IPPrefix, noPrefix, endPrefix));
    }
}