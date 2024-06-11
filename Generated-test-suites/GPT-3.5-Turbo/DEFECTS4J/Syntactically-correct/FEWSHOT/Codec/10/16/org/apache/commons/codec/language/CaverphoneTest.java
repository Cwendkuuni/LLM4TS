import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        // Test cases for txt = null
        assertEquals("1111111111", new Caverphone().caverphone(null));

        // Test cases for empty txt
        assertEquals("1111111111", new Caverphone().caverphone(""));

        // Test cases for special characters
        assertEquals("rfqrfqwwwwwwwwwww", new Caverphone().caverphone("!@#$%^&*()_+"));
        
        // Test cases for lowercase conversion
        assertEquals("kaffrkinlrknt", new Caverphone().caverphone("KaFFerkinLErKant"));

        // Test cases for removal of non-alphabetic characters
        assertEquals("rf", new Caverphone().caverphone("rf1234"));
        
        // Test cases for final e removal
        assertEquals("wh", new Caverphone().caverphone("whe"));

        // Test cases for various start options
        assertEquals("cobuyton", new Caverphone().caverphone("coughbuyton"));
        assertEquals("ronbuyton", new Caverphone().caverphone("roughbuyton"));
        assertEquals("tonbuyton", new Caverphone().caverphone("toughbuyton"));
        assertEquals("enouhtbayton", new Caverphone().caverphone("enoughbuyton"));  
        assertEquals("trouhtbaton", new Caverphone().caverphone("troughbaton"));  
        assertEquals("nuyman", new Caverphone().caverphone("gnuyman"));  

        // Test cases for replacements
        assertEquals("2nkurytyliyn", new Caverphone().caverphone("cqurytionline"));
        assertEquals("s2lforkurily", new Caverphone().caverphone("selforchilis"));

        // Test cases for removals
        assertEquals("3n 0lf p3rty", new Caverphone().caverphone("an elf party"));

        // Test cases for putting ten 1s on the end
        assertEquals("an elf par111111 1111", new Caverphone().caverphone("an elf par"));

        // Test cases for taking first six characters as the code
        assertEquals("an elf pa", new Caverphone().caverphone("an elf party"));
    }

    @Test
    public void testEncode() {
        // Test cases for txt = null
        assertEquals("1111111111", new Caverphone().encode(null));

        // Test cases for empty txt
        assertEquals("1111111111", new Caverphone().encode(""));

        // Test cases for special characters
        assertEquals("rfqrfqwwwwwwwwwww", new Caverphone().encode("!@#$%^&*()_+"));
        
        // Test cases for lowercase conversion
        assertEquals("kaffrkinlrknt", new Caverphone().encode("KaFFerkinLErKant"));

        // Test cases for removal of non-alphabetic characters
        assertEquals("rf", new Caverphone().encode("rf1234"));
        
        // Test cases for final e removal
        assertEquals("wh", new Caverphone().encode("whe"));

        // Test cases for various start options
        assertEquals("cobuyton", new Caverphone().encode("coughbuyton"));
        assertEquals("ronbuyton", new Caverphone().encode("roughbuyton"));
        assertEquals("tonbuyton", new Caverphone().encode("toughbuyton"));
        assertEquals("enouhtbayton", new Caverphone().encode("enoughbuyton"));  
        assertEquals("trouhtbaton", new Caverphone().encode("troughbaton"));  
        assertEquals("nuyman", new Caverphone().encode("gnuyman"));  

        // Test cases for replacements
        assertEquals("2nkurytyliyn", new Caverphone().encode("cqurytionline"));
        assertEquals("s2lforkurily", new Caverphone().encode("selforchilis"));

        // Test cases for removals
        assertEquals("3n 0lf p3rty", new Caverphone().encode("an elf party"));

        // Test cases for putting ten 1s on the end
        assertEquals("an elf par1111111111", new Caverphone().encode("an elf par"));

        // Test cases for taking first six characters as the code
        assertEquals("an elf pa", new Caverphone().encode("an elf party"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        // Test cases for equality of caverphones
        assertEquals(true, new Caverphone().isCaverphoneEqual("an elf party", "an elf perty"));
        assertEquals(false, new Caverphone().isCaverphoneEqual("an elf party", "a nelf party"));
    }
}