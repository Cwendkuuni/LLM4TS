import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GeneratorBaseTest {
  
  private GeneratorBase generator;
  
  @Before
  public void setUp() {
    generator = new GeneratorBaseImpl();
  }
  
  @Test
  public void testVersion() {
    assertEquals(null, generator.version());
  }
  
  @Test
  public void testGetCurrentValue() {
    assertEquals(null, generator.getCurrentValue());
  }
  
  @Test
  public void testSetCurrentValue() {
    generator.setCurrentValue("Test");
    assertEquals("Test", generator.getCurrentValue());
  }
  
  @Test
  public void testIsEnabled() {
    assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
  }
  
  @Test
  public void testGetFeatureMask() {
    assertEquals(0, generator.getFeatureMask());
  }
  
  @Test
  public void testEnable() {
    generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
    assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
  }
  
  @Test
  public void testDisable() {
    generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
    assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
  }
  
  @Test
  public void testSetFeatureMask() {
    generator.setFeatureMask(10);
    assertEquals(10, generator.getFeatureMask());
  }
  
  @Test
  public void testOverrideStdFeatures() {
    generator.overrideStdFeatures(15, 10);
    assertEquals(15, generator.getFeatureMask());
  }
  
  @Test
  public void testUseDefaultPrettyPrinter() {
    assertNull(generator.getPrettyPrinter());
    generator.useDefaultPrettyPrinter();
    assertNotNull(generator.getPrettyPrinter());
  }
  
  @Test
  public void testSetCodec() {
    assertNull(generator.getCodec());
    generator.setCodec(new ObjectCodecImpl());
    assertNotNull(generator.getCodec());
  }
  
  @Test
  public void testGetOutputContext() {
    assertNotNull(generator.getOutputContext());
  }
  
  @Test
  public void testWriteFieldNameString() {
    // Test implementation not provided  
  }
  
  @Test
  public void testWriteFieldNameSerializableString() {
    // Test implementation not provided  
  }
  
  @Test
  public void testWriteString() {
    // Test implementation not provided 
  }
  
  @Test
  public void testWriteRaw() {
    // Test implementation not provided 
  }
  
  @Test
  public void testWriteRawValueString() {
    // Test implementation not provided 
  }
  
  @Test
  public void testWriteRawValueStringIntInt() {
    // Test implementation not provided 
  }
  
  @Test
  public void testWriteRawValueCharArrayIntInt() {
    // Test implementation not provided 
  }
  
  @Test
  public void testWriteRawValueSerializableString() {
    // Test implementation not provided 
  }
  
  @Test
  public void testWriteBinary() {
    // Test implementation not provided 
  }
  
  @Test
  public void testWriteObject() {
    // Test implementation not provided 
  }
  
  @Test
  public void testWriteTree() {
    // Test implementation not provided 
  }
  
  @Test
  public void testFlush() {
    // Test implementation not provided 
  }
  
  @Test
  public void testClose() {
    assertFalse(generator.isClosed());
    generator.close();
    assertTrue(generator.isClosed());
  }
  
  @Test
  public void testIsClosed() {
    assertFalse(generator.isClosed());
  }
  
}