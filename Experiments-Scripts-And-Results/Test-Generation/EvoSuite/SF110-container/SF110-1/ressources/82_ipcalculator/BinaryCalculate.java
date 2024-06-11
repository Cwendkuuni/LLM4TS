package ipac;

import java.math.*;
import java.text.*;
import java.util.*;

public class BinaryCalculate
{
    boolean isIPv4;
    boolean isIPv6;
    String currentIP;
    String fullReassembler;
    String DELIMITER;
    int lengthOfToken;
    String IPRangeAddress;
    String usableRange;
    int semiCol;
    String prefix;
    BigInteger totalIPAddresses;
    int MAXPREFIX;
    
    public BinaryCalculate() {
        this.isIPv4 = false;
        this.isIPv6 = false;
        this.currentIP = "";
        this.fullReassembler = "";
        this.DELIMITER = "";
        this.lengthOfToken = 0;
        this.IPRangeAddress = "";
        this.usableRange = "";
        this.semiCol = 0;
        this.prefix = "";
        this.MAXPREFIX = 0;
    }
    
    public String addBinary(String num1, String num2) {
        boolean carry = false;
        int size1 = num1.length() - 1;
        int size2 = num2.length() - 1;
        String sum = "";
        if (size2 > size1) {
            final String s = num1;
            num1 = num2;
            num2 = s;
            final int n = size1;
            size1 = size2;
            size2 = n;
        }
        while (size2 >= 0) {
            if (num1.charAt(size1) == '0' && num2.charAt(size2) == '0') {
                if (!carry) {
                    sum = "0" + sum;
                }
                else {
                    sum = "1" + sum;
                }
                carry = false;
            }
            else if (num1.charAt(size1) == '1' && num2.charAt(size2) == '0') {
                if (!carry) {
                    sum = "1" + sum;
                }
                else {
                    sum = "0" + sum;
                    carry = true;
                }
            }
            else if (num1.charAt(size1) == '0' && num2.charAt(size2) == '1') {
                if (!carry) {
                    sum = "1" + sum;
                }
                else {
                    sum = "0" + sum;
                    carry = true;
                }
            }
            else if (num1.charAt(size1) == '1' && num2.charAt(size2) == '1') {
                if (!carry) {
                    sum = "0" + sum;
                }
                else {
                    sum = "1" + sum;
                }
                carry = true;
            }
            --size1;
            --size2;
        }
        while (size1 >= 0) {
            if (num1.charAt(size1) == '0') {
                if (!carry) {
                    sum = "0" + sum;
                }
                else {
                    sum = "1" + sum;
                }
                carry = false;
            }
            else if (num1.charAt(size1) == '1') {
                if (!carry) {
                    sum = "1" + sum;
                }
                else {
                    sum = "0" + sum;
                    carry = true;
                }
            }
            --size1;
        }
        if (carry) {
            sum = "1" + sum;
        }
        return sum;
    }
    
    public String subBinary(final String num1, final String num2) {
        String diff = "";
        String twosComp = "";
        for (int size = num1.length() - 1; size >= 0; --size) {
            if (num1.charAt(size) == '0') {
                twosComp = "1" + twosComp;
            }
            else {
                twosComp = "0" + twosComp;
            }
        }
        twosComp = "0" + twosComp;
        twosComp = this.addBinary(twosComp, "1");
        diff = this.addBinary(twosComp, num2);
        return diff.substring(1);
    }
    
    public String IPCalculate(final String IP, final String IPPrefix) {
        String outputText = "";
        this.checkIPType(IP.length() - 1);
        final String subnet = this.convert(IP);
        final String netmask = this.getNetmask(IPPrefix);
        this.calculate(IP, IPPrefix);
        outputText = outputText + "Total Range: " + this.IPRangeAddress + "\n";
        outputText = outputText + "Usable Range: " + this.usableRange + "\n";
        final NumberFormat formatter = new DecimalFormat("###,###,###,###,###,###,###,###,###,###,###,###,###");
        outputText += "\n";
        outputText = outputText + "Total usable IP Addresses : " + formatter.format(this.totalIPAddresses) + "\n";
        outputText = outputText + "Subnet: " + subnet + "\n";
        outputText = outputText + "Binary Subnet: " + this.getBinaryIP(IP) + "\n";
        outputText = outputText + "Broadcast Address: " + this.currentIP + "\n";
        outputText = outputText + "Prefix: " + this.prefix + "\n";
        this.checkIPType(netmask.length() - 1);
        final String outputNetmask = this.convert(netmask);
        outputText = outputText + "Netmask: " + outputNetmask + "\n";
        final String binaryNetmask = this.getBinaryIP(netmask);
        outputText = outputText + "Binary Netmask: " + binaryNetmask;
        return outputText;
    }
    
    private String getBinaryIP(final String binaryIP) {
        String output = "";
        this.checkIPType(binaryIP.length() - 1);
        for (int i = 0; i < this.MAXPREFIX; ++i) {
            output += binaryIP.charAt(i);
            if (i == this.semiCol) {
                if (i != binaryIP.length() - 1) {
                    output += this.DELIMITER;
                }
                this.semiCol += this.lengthOfToken;
            }
        }
        return output;
    }
    
    private String getNetmask(final String binaryIP) {
        String invertedIPPrefix = "";
        for (int i = 0; i < this.MAXPREFIX; ++i) {
            if (binaryIP.charAt(i) == '0') {
                invertedIPPrefix += "1";
            }
            else {
                invertedIPPrefix += "0";
            }
        }
        return invertedIPPrefix;
    }
    
    private void calculate(String subnet, final String noOfIPAddresses) {
        this.currentIP = "";
        final String number1 = subnet;
        String usableIP = "";
        final String IP = this.addBinary(subnet, noOfIPAddresses);
        this.checkIPType(subnet.length() - 1);
        String addSubBinary = "";
        for (int i = 0; i < this.MAXPREFIX - 1; ++i) {
            addSubBinary += "0";
        }
        addSubBinary += "1";
        subnet = this.addBinary(addSubBinary, subnet);
        usableIP = this.convert(subnet);
        subnet = this.subBinary(addSubBinary, subnet);
        this.checkIPType(subnet.length() - 1);
        this.currentIP = this.convert(subnet);
        this.fullReassembler = "";
        this.semiCol = this.lengthOfToken - 1;
        this.IPRangeAddress = this.currentIP + " -- ";
        this.usableRange = usableIP + " -- ";
        this.currentIP = "";
        String currentIPString = "";
        if (IP.length() > 128) {
            this.IPRangeAddress += "FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF";
            this.currentIP = "FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF";
        }
        else if (IP.length() > 32 && IP.length() < 128) {
            this.IPRangeAddress += "255.255.255.255";
            this.currentIP = "255.255.255.255";
        }
        else {
            final String IPRangeEnd = this.subBinary(addSubBinary, IP);
            final String endIP = this.convert(IPRangeEnd);
            this.usableRange += endIP;
            this.checkIPType(subnet.length() - 1);
            this.currentIP = this.convert(IP);
            this.IPRangeAddress += this.currentIP;
        }
        currentIPString = this.subBinary(number1, this.convertToBinary(this.currentIP));
        this.totalIPAddresses = new BigInteger(currentIPString, 2);
        this.totalIPAddresses = this.totalIPAddresses.add(new BigInteger("1"));
        int prefixInt = 0;
        if (this.totalIPAddresses.doubleValue() < 2.0) {
            prefixInt = this.MAXPREFIX;
        }
        else if (this.totalIPAddresses.doubleValue() == 2.0) {
            prefixInt = this.MAXPREFIX - 2;
        }
        else if (this.isIPv6) {
            final String currentIPString2 = currentIPString.substring(0, 64);
            final BigInteger totalIPAddresses1 = new BigInteger(currentIPString2, 2);
            double prefixInt2 = 0.0;
            if (totalIPAddresses1.doubleValue() > 0.0) {
                prefixInt2 = Math.log(totalIPAddresses1.doubleValue()) / Math.log(2.0);
            }
            final String currentIPString3 = currentIPString.substring(64, 128);
            final BigInteger totalIPAddresses2 = new BigInteger(currentIPString3, 2);
            final double prefixInt3 = Math.log(totalIPAddresses2.doubleValue()) / Math.log(2.0);
            prefixInt = this.MAXPREFIX - (int)Math.ceil(prefixInt2) - (int)Math.round(prefixInt3);
        }
        else if (this.isIPv4) {
            prefixInt = this.MAXPREFIX - (int)Math.ceil(Math.log(this.totalIPAddresses.doubleValue()) / Math.log(2.0));
        }
        this.prefix = "/" + prefixInt;
        this.totalIPAddresses = this.totalIPAddresses.subtract(new BigInteger("2"));
    }
    
    public void prefixInPrefixCalculate(String IP, final String IPPrefix, final int noPrefix, final String endPrefix) {
        try {
            final String[][] output = new String[noPrefix + 8][2];
            this.checkIPType(IP.length() - 1);
            final String subnet = this.convert(IP);
            final String netmask = this.getNetmask(IPPrefix);
            this.calculate(IP, IPPrefix);
            for (int i = 0; i < noPrefix; ++i) {
                this.calculate(IP, IPPrefix);
                output[i][0] = this.getStartIP() + " " + this.prefix;
                output[i][1] = this.IPRangeAddress;
                IP = this.addBinary(this.fullReassembler.substring(this.MAXPREFIX, this.fullReassembler.length()), Integer.toBinaryString(1));
                this.fullReassembler = "";
            }
            output[noPrefix + 1][0] = "Total usable IP Addresses :";
            final NumberFormat formatter = new DecimalFormat("###,###,###,###,###,###,###,###,###,###,###,###,###");
            output[noPrefix + 1][1] = formatter.format(this.totalIPAddresses.multiply(new BigInteger(noPrefix + ""))) + " ";
            output[noPrefix + 2][0] = "Subnet:";
            output[noPrefix + 2][1] = subnet;
            final StringTokenizer subnetTokens = new StringTokenizer(this.convertToBinary(subnet), this.DELIMITER);
            String paddedSubnet = "";
            while (subnetTokens.hasMoreTokens()) {
                String unpaddedSubnet;
                for (unpaddedSubnet = subnetTokens.nextToken(); unpaddedSubnet.length() < this.lengthOfToken; unpaddedSubnet = "0" + unpaddedSubnet) {}
                paddedSubnet += unpaddedSubnet;
            }
            output[noPrefix + 3][0] = "Binary Subnet:";
            output[noPrefix + 3][1] = this.getBinaryIP(paddedSubnet);
            output[noPrefix + 4][0] = "Broadcast Address:";
            output[noPrefix + 4][1] = this.currentIP;
            output[noPrefix + 5][0] = "Prefix:";
            output[noPrefix + 5][1] = endPrefix;
            this.checkIPType(netmask.length() - 1);
            output[noPrefix + 6][0] = "Netmask";
            output[noPrefix + 6][1] = this.convert(netmask);
            output[noPrefix + 7][0] = "Binary Netmask:";
            output[noPrefix + 7][1] = this.getBinaryIP(netmask);
            new Output(output);
        }
        catch (OutOfMemoryError e) {
            new IPv4().displayError("Your computer does not have sufficent memory to process this query.");
        }
    }
    
    private void checkIPType(final int size1) {
        if (size1 == 31) {
            this.isIPv4 = true;
            this.lengthOfToken = 8;
            this.DELIMITER = ".";
            this.MAXPREFIX = 32;
        }
        else if (size1 == 127) {
            this.isIPv6 = true;
            this.lengthOfToken = 16;
            this.DELIMITER = ":";
            this.MAXPREFIX = 128;
        }
        this.semiCol = this.lengthOfToken - 1;
    }
    
    private String convert(final String binaryIP) {
        String outputIP = "";
        String reassembler = "";
        for (int i = 0; i < binaryIP.length(); ++i) {
            reassembler += binaryIP.charAt(i);
            this.fullReassembler += binaryIP.charAt(i);
            if (i == this.semiCol) {
                if (this.isIPv4) {
                    outputIP += Integer.parseInt(reassembler, 2);
                    reassembler = "";
                }
                else if (this.isIPv6) {
                    outputIP += Integer.toHexString(Integer.parseInt(reassembler, 2));
                    reassembler = "";
                }
                if (i != binaryIP.length() - 1) {
                    outputIP += this.DELIMITER;
                }
                this.semiCol += this.lengthOfToken;
            }
        }
        return outputIP;
    }
    
    private String convertToBinary(final String IP) {
        final StringTokenizer octetToken = new StringTokenizer(IP);
        String output = "";
        int radix = 0;
        if (this.isIPv4) {
            radix = 10;
        }
        else if (this.isIPv6) {
            radix = 16;
        }
        while (octetToken.hasMoreTokens()) {
            String out;
            for (out = Integer.toBinaryString(Integer.parseInt(octetToken.nextToken(".:"), radix)); out.length() < this.lengthOfToken; out = "0" + out) {}
            output += out;
        }
        return output;
    }
    
    private String getStartIP() {
        final StringTokenizer tokens = new StringTokenizer(this.IPRangeAddress, " -");
        return tokens.nextToken();
    }
}
