import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {
  
  private static final int VALUE = 10;
  private static final long INSTANT = 123456789L;
  
  private LenientDateTimeField lenientDateTimeField;
  private DateTimeField strictDateTimeField;
  private Chronology base;
  
  @Test
  public void testGetInstance() {
    strictDateTimeField = new StrictDateTimeField();
    base = new Chronology();
    
    DateTimeField result = LenientDateTimeField.getInstance(strictDateTimeField, base);
    
    assertTrue(result instanceof LenientDateTimeField);
  }
  
  @Test
  public void testGetInstance_withNullField() {
    DateTimeField result = LenientDateTimeField.getInstance(null, base);
    
    assertNull(result);
  }
  
  @Test
  public void testGetInstance_withLenientField() {
    strictDateTimeField = new StrictDateTimeField();
    strictDateTimeField.setLenient(true);
    base = new Chronology();
    
    DateTimeField result = LenientDateTimeField.getInstance(strictDateTimeField, base);
    
    assertEquals(result, strictDateTimeField);
  }
  
  @Test
  public void testSet() {
    strictDateTimeField = new StrictDateTimeField();
    base = new Chronology();
    lenientDateTimeField = new LenientDateTimeField(strictDateTimeField, base);
    
    long result = lenientDateTimeField.set(INSTANT, VALUE);
    
    assertEquals(result, INSTANT);
  }
  
  @Test
  public void testSet_withNonStrictField() {
    strictDateTimeField = new StrictDateTimeField();
    strictDateTimeField.setLenient(true);
    base = new Chronology();
    lenientDateTimeField = new LenientDateTimeField(strictDateTimeField, base);
    
    long result = lenientDateTimeField.set(INSTANT, VALUE);
    
    assertEquals(result, INSTANT + VALUE);
  }
  
  @Test
  public void testIsLenient() {
    strictDateTimeField = new StrictDateTimeField();
    base = new Chronology();
    lenientDateTimeField = new LenientDateTimeField(strictDateTimeField, base);
    
    boolean result = lenientDateTimeField.isLenient();
    
    assertTrue(result);
  }
  
  @Test
  public void testIsLenient_withNonStrictField() {
    strictDateTimeField = new StrictDateTimeField();
    strictDateTimeField.setLenient(true);
    base = new Chronology();
    lenientDateTimeField = new LenientDateTimeField(strictDateTimeField, base);
    
    boolean result = lenientDateTimeField.isLenient();
    
    assertTrue(result);
  }
  
}