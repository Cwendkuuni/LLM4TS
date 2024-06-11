import org.junit.Test;

 import static org.junit.Assert.*;

 public class CacheBuilderSpecTest {

     @Test
     public void testParse() {
         CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100,concurrencyLevel=4");
         assertEquals(Integer.valueOf(100), spec.maximumSize);
         assertEquals(Integer.valueOf(4), spec.concurrencyLevel);
     }

     @Test
     public void testDisableCaching() {
         CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
         assertEquals(Long.valueOf(0), spec.maximumSize);
     }

     @Test
     public void testToCacheBuilder() {
         CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100,concurrencyLevel=4");

         CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
         assertNotNull(builder);
         assertEquals(100, builder.maximumSize());
         assertEquals(4, builder.concurrencyLevel());
     }

     @Test
     public void testToParsableString() {
         CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100,concurrencyLevel=4");
         String parsableString = spec.toParsableString();
         assertEquals("maximumSize=100,concurrencyLevel=4", parsableString);
     }

     @Test
     public void testHashCode() {
         CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100,concurrencyLevel=4");
         CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100,concurrencyLevel=4");
         CacheBuilderSpec spec3 = new CacheBuilderSpec("maximumSize=50,concurrencyLevel=4");

         assertEquals(spec1.hashCode(), spec2.hashCode());
         assertNotEquals(spec1.hashCode(), spec3.hashCode());
     }

     @Test
     public void testEquals() {
         CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100,concurrencyLevel=4");
         CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100,concurrencyLevel=4");
         CacheBuilderSpec spec3 = new CacheBuilderSpec("maximumSize=50,concurrencyLevel=4");

         assertTrue(spec1.equals(spec2));
         assertFalse(spec1.equals(spec3));
         assertFalse(spec1.equals(null));
         assertFalse(spec1.equals("test"));
     }

 }