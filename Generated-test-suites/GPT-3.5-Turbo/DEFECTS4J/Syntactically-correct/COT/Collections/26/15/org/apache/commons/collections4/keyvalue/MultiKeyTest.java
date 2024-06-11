import org.junit.Assert;
import org.junit.Test;

public class MultiKeyTest {
    
    @Test
    public void testConstructorWithTwoKeys() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        
        // Act
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        // Assert
        Assert.assertArrayEquals(new String[]{key1, key2}, multiKey.getKeys());
    }
    
    @Test
    public void testConstructorWithThreeKeys() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        
        // Act
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        // Assert
        Assert.assertArrayEquals(new String[]{key1, key2, key3}, multiKey.getKeys());
    }
    
    @Test
    public void testConstructorWithFourKeys() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        
        // Act
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        // Assert
        Assert.assertArrayEquals(new String[]{key1, key2, key3, key4}, multiKey.getKeys());
    }
    
    @Test
    public void testConstructorWithFiveKeys() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        
        // Act
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        // Assert
        Assert.assertArrayEquals(new String[]{key1, key2, key3, key4, key5}, multiKey.getKeys());
    }
    
    @Test
    public void testConstructorWithArray() {
        // Arrange
        String[] keys = new String[]{"key1", "key2", "key3"};
        
        // Act
        MultiKey<String> multiKey = new MultiKey<>(keys);
        
        // Assert
        Assert.assertArrayEquals(keys, multiKey.getKeys());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        // Arrange
        String[] keys = null;
        
        // Act
        MultiKey<String> multiKey = new MultiKey<>(keys);
        
        // Assert (Expected Exception)
    }
    
    @Test
    public void testGetSize() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        // Act
        int size = multiKey.size();
        
        // Assert
        Assert.assertEquals(2, size);
    }
    
    @Test
    public void testGetKey() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        // Act
        String key = multiKey.getKey(1);
        
        // Assert
        Assert.assertEquals(key2, key);
    }
    
    @Test
    public void testEquals() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey3 = new MultiKey<>(key2, key2);
        
        // Act & Assert
        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertTrue(multiKey2.equals(multiKey1));
        Assert.assertFalse(multiKey1.equals(multiKey3));
        Assert.assertFalse(multiKey3.equals(multiKey1));
    }
    
    @Test
    public void testHashCode() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        
        // Act
        int hashCode1 = multiKey1.hashCode();
        int hashCode2 = multiKey2.hashCode();
        
        // Assert
        Assert.assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testToString() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        // Act
        String str = multiKey.toString();
        
        // Assert
        Assert.assertEquals("MultiKey[key1, key2]", str);
    }
}