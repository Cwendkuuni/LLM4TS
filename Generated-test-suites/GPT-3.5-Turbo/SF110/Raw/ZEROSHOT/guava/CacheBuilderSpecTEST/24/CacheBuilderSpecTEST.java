import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

@Test
public void testParse() {
   CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,concurrencyLevel=5,recordStats");
   assertEquals(10, spec.initialCapacity.intValue());
   assertEquals(100, spec.maximumSize.longValue());
   assertEquals(5, spec.concurrencyLevel.intValue());
   assertTrue(spec.recordStats);
}

@Test
public void testDisableCaching() {
   CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
   assertEquals(0, spec.maximumSize.longValue());
}

@Test
public void testToCacheBuilder() {
   CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,concurrencyLevel=5,recordStats");
   CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
   assertNotNull(builder);
}

@Test
public void testToParsableString() {
   CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,concurrencyLevel=5,recordStats");
   String parsableString = spec.toParsableString();
   assertEquals("initialCapacity=10,maximumSize=100,concurrencyLevel=5,recordStats", parsableString);
}

@Test
public void testEquals() {
   CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
   CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
   assertTrue(spec1.equals(spec2));
}

@Test
public void testHashCode() {
   CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
   CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
   assertEquals(spec1.hashCode(), spec2.hashCode());
}

}