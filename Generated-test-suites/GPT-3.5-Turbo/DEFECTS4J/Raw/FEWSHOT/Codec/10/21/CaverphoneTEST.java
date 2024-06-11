import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        // Test case 1: Empty string
        String result1 = new Caverphone().caverphone("");
        Assert.assertEquals("1111111111", result1);
        
        // Test case 2: Null string
        String result2 = new Caverphone().caverphone(null);
        Assert.assertEquals("1111111111", result2);
        
        // Test case 3: Lowercase string with all letters 
        String result3 = new Caverphone().caverphone("abcdefghi");
        Assert.assertEquals("A111111111", result3);
        
        // Test case 4: Lowercase string with special characters and digits
        String result4 = new Caverphone().caverphone("ab!cde#fgh1");
        Assert.assertEquals("A111111111", result4);
        
        // Test case 5: Lowercase string with only vowels
        String result5 = new Caverphone().caverphone("aeiou");
        Assert.assertEquals("33AAAAAA11", result5);
        
        // Test case 6: Lowercase string with only consonants
        String result6 = new Caverphone().caverphone("bcdfghjklmnpqrstvwxyz");
        Assert.assertEquals("KKKKKKKKKKK", result6);
        
        // Test case 7: Lowercase string with mixed vowels and consonants
        String result7 = new Caverphone().caverphone("aabcdefbcdfghijklmnopq");
        Assert.assertEquals("A11111KKK11", result7);
        
        // Test case 8: Lowercase string with specific replacements
        String result8 = new Caverphone().caverphone("cqci cetych");
        Assert.assertEquals("KKS S33F111", result8);
    }

    @Test
    public void testEncode() {
        // Test case 1: Empty string
        String result1 = new Caverphone().encode("");
        Assert.assertEquals("1111111111", result1);
        
        // Test case 2: Null string
        String result2 = new Caverphone().encode(null);
        Assert.assertEquals("1111111111", result2);
        
        // Test case 3: Lowercase string with all letters 
        String result3 = new Caverphone().encode("abcdefghi");
        Assert.assertEquals("A111111111", result3);
        
        // Test case 4: Lowercase string with special characters and digits
        String result4 = new Caverphone().encode("ab!cde#fgh1");
        Assert.assertEquals("A111111111", result4);
        
        // Test case 5: Lowercase string with only vowels
        String result5 = new Caverphone().encode("aeiou");
        Assert.assertEquals("33AAAAAA11", result5);
        
        // Test case 6: Lowercase string with only consonants
        String result6 = new Caverphone().encode("bcdfghjklmnpqrstvwxyz");
        Assert.assertEquals("KKKKKKKKKKK", result6);
        
        // Test case 7: Lowercase string with mixed vowels and consonants
        String result7 = new Caverphone().encode("aabcdefbcdfghijklmnopq");
        Assert.assertEquals("A11111KKK11", result7);
        
        // Test case 8: Lowercase string with specific replacements
        String result8 = new Caverphone().encode("cqci cetych");
        Assert.assertEquals("KKS S33F111", result8);
    }

    @Test
    public void testIsCaverphoneEqual() {
        // Test case 1: Strings are equal
        boolean result1 = new Caverphone().isCaverphoneEqual("hello", "hello");
        Assert.assertTrue(result1);
        
        // Test case 2: Strings are different
        boolean result2 = new Caverphone().isCaverphoneEqual("hello", "world");
        Assert.assertFalse(result2);
    }

}