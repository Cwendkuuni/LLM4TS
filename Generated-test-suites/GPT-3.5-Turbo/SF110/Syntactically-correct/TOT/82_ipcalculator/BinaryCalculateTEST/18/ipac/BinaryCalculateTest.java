import static org.junit.Assert.*;
import org.junit.*;

public class BinaryCalculateTest {

    private BinaryCalculate calculator;

    @Before
    public void setUp() {
        calculator = new BinaryCalculate();
    }

    @Test
    public void testAddBinary() {
        String num1 = "1010";
        String num2 = "1011";
        String expectedSum = "10101";

        String sum = calculator.addBinary(num1, num2);

        assertEquals(expectedSum, sum);
    }

    @Test
    public void testSubBinary() {
        String num1 = "1010";
        String num2 = "1011";
        String expectedDiff = "1";

        String diff = calculator.subBinary(num1, num2);

        assertEquals(expectedDiff, diff);
    }

    @Test
    public void testIPCalculate() {
        String IP = "192.168.0.1";
        String IPPrefix = "24";
        String expectedOutput = "Total Range: 192.168.0.0 -- 192.168.0.255\n" +
                                "Usable Range: 192.168.0.1 -- 192.168.0.254\n" +
                                "\n" +
                                "Total usable IP Addresses : 254\n" +
                                "Subnet: 192.168.0.1\n" +
                                "Binary Subnet: 11000000.10101000.00000000.00000001\n" +
                                "Broadcast Address: 192.168.0.255\n" +
                                "Prefix: /24\n" +
                                "Netmask: 255.255.255.0\n" +
                                "Binary Netmask: 11111111.11111111.11111111.00000000";

        String output = calculator.IPCalculate(IP, IPPrefix);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        String IP = "192.168.0.1";
        String IPPrefix = "24";
        int noPrefix = 2;
        String endPrefix = "26";
        String[][] expectedOutput = {    {"192.168.0.0 /26", "192.168.0.0 -- 192.168.0.63"},
                                        {"192.168.0.64 /26", "192.168.0.64 -- 192.168.0.127"},
                                        {"Total usable IP Addresses :", "759 "},
                                        {"Subnet:", "192.168.0.1"},
                                        {"Binary Subnet:", "11000000.10101000.00000000.00000001"},
                                        {"Broadcast Address:", "192.168.0.255"},
                                        {"Prefix:", "26"},
                                        {"Netmask", "255.255.255.192"},
                                        {"Binary Netmask:", "11111111.11111111.11111111.11000000"}};

        calculator.prefixInPrefixCalculate(IP, IPPrefix, noPrefix, endPrefix);
        String[][] output = Output.getOutput();

        assertArrayEquals(expectedOutput, output);
    }

}