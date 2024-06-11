import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
  
  private CacheBuilderSpec cacheBuilderSpec;
  
  @Before
  public void setUp() {
    cacheBuilderSpec = new CacheBuilderSpec("");
  }
  
  @Test
  public void parse_emptyCacheBuilderSpecification_doesNotThrowException() {
    cacheBuilderSpec.parse("");
    assertNotNull(cacheBuilderSpec.toParsableString());
  }
  
  @Test
  public void parse_validCacheBuilderSpecification_parsesSpecCorrectly() {
    cacheBuilderSpec.parse("maximumSize=100, expireAfterAccess=5d");
    assertEquals("maximumSize=100, expireAfterAccess=5d", cacheBuilderSpec.toParsableString());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void parse_incorrectCacheBuilderSpecification_throwsException() {
    cacheBuilderSpec.parse("invalidKey=10");
  }
  
  @Test
  public void disableCaching_returnedCacheBuilderSpecification_hasMaximumSizeSetToZero() {
    cacheBuilderSpec = CacheBuilderSpec.disableCaching();
    assertEquals("maximumSize=0", cacheBuilderSpec.toParsableString());
  }
  
  @Test
  public void toCacheBuilder_returnsCacheBuilderWithProperSettings() {
    cacheBuilderSpec.parse("maximumSize=100, expireAfterAccess=5d");
    CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
    
    assertNotNull(cacheBuilder);
    assertEquals(100, cacheBuilder.maximumSize());
    assertEquals(5L, cacheBuilder.accessExpirationDuration());
    assertEquals(TimeUnit.DAYS, cacheBuilder.accessExpirationTimeUnit());
  }
  
  @Test
  public void toString_returnsProperStringRepresentation() {
    cacheBuilderSpec.parse("maximumSize=100, expireAfterAccess=5d");
    String specString = cacheBuilderSpec.toString();
    
    assertNotNull(specString);
    assertTrue(specString.contains("maximumSize=100"));
    assertTrue(specString.contains("expireAfterAccess=5d"));
  }
  
  @Test
  public void hashCode_forEqualCacheBuilderSpecObjects_isEqual() {
    cacheBuilderSpec.parse("maximumSize=100, expireAfterAccess=5d");
    CacheBuilderSpec otherSpec = new CacheBuilderSpec("maximumSize=100, expireAfterAccess=5d");
    
    assertEquals(cacheBuilderSpec.hashCode(), otherSpec.hashCode());
  }
  
  @Test
  public void equals_forEqualCacheBuilderSpecObjects_returnsTrue() {
    cacheBuilderSpec.parse("maximumSize=100, expireAfterAccess=5d");
    CacheBuilderSpec otherSpec = new CacheBuilderSpec("maximumSize=100, expireAfterAccess=5d");
    
    assertTrue(cacheBuilderSpec.equals(otherSpec));
  }
  
  @Test
  public void equals_forUnequalCacheBuilderSpecObjects_returnsFalse() {
    cacheBuilderSpec.parse("maximumSize=100, expireAfterAccess=5d");
    CacheBuilderSpec otherSpec = new CacheBuilderSpec("maximumSize=100, expireAfterAccess=6d");
    
    assertFalse(cacheBuilderSpec.equals(otherSpec));
  }
  
  @Test
  public void toParsableString_returnsStringRepresentationOfSpec() {
    cacheBuilderSpec.parse("maximumSize=100, expireAfterAccess=5d");
    
    String parsableString = cacheBuilderSpec.toParsableString();
    
    assertNotNull(parsableString);
    assertTrue(parsableString.contains("maximumSize=100"));
    assertTrue(parsableString.contains("expireAfterAccess=5d"));
  }
}