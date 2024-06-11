
package org.apache.commons.codec.language;

import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Test cases from Caverphone 2.0 specification
        Assert.assertEquals("BBBBN11111", caverphone.caverphone("Knuth"));
        Assert.assertEquals("BBBSN11111", caverphone.caverphone("Kant"));
        Assert.assertEquals("KMT1111111", caverphone.caverphone("Knut"));
        
        // Additional test cases
        Assert.assertEquals("AMTTN11111", caverphone.caverphone("Adamson"));
        Assert.assertEquals("RYDT111111", caverphone.caverphone("Robertson"));
        Assert.assertEquals("SN33311111", caverphone.caverphone("Smith"));
        Assert.assertEquals("WLAK111111", caverphone.caverphone("Walker"));
        Assert.assertEquals("FKMM211111", caverphone.caverphone("Ferguson"));

    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.encode(new Object()); // Test for non-string object
            Assert.fail("EncoderException not thrown for non-string object");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        Assert.assertEquals("BBBBN11111", caverphone.encode("Knuth"));
        Assert.assertEquals("BBBSN11111", caverphone.encode("Kant"));
        Assert.assertEquals("KMT1111111", caverphone.encode("Knut"));
        Assert.assertEquals("AMTTN11111", caverphone.encode("Adamson"));
        Assert.assertEquals("RYDT111111", caverphone.encode("Robertson"));
        Assert.assertEquals("SN33311111", caverphone.encode("Smith"));
        Assert.assertEquals("WLAK111111", caverphone.encode("Walker"));
        Assert.assertEquals("FKMM211111", caverphone.encode("Ferguson"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test cases from Caverphone 2.0 specification
        Assert.assertTrue(caverphone.isCaverphoneEqual("Knuth", "Knuth")); // Same name
        Assert.assertFalse(caverphone.isCaverphoneEqual("Knuth", "Kant")); // Different name
        
        // Additional test cases
        Assert.assertFalse(caverphone.isCaverphoneEqual("Adamson", "Robertson"));
        Assert.assertFalse(caverphone.isCaverphoneEqual("Smith", "Walker"));
        Assert.assertFalse(caverphone.isCaverphoneEqual("Ferguson", "Ferguson"));
        Assert.assertTrue(caverphone.isCaverphoneEqual("ABBAG", "ABBEG"));
    }

}
