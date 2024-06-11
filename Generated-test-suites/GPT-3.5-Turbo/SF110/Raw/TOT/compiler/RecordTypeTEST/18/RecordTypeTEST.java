import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  private RecordType recordType;
  
  @Before
  public void setUp() {
    recordType = new RecordType(null, null, true);
  }
  
  @Test
  public void testIsSynthetic() {
    assertFalse(recordType.isSynthetic());
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper_sameRecordType_true() {
    RecordType otherRecord = new RecordType(null, null, true);
    
    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, null));
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper_differentRecordType_false() {
    RecordType otherRecord = new RecordType(null, null, false);
    
    assertFalse(recordType.checkRecordEquivalenceHelper(otherRecord, null));
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper_samePropertiesAndTypes_true() {
    // Arrange
    RecordType otherRecord = new RecordType(null, null, true);
    recordType.defineProperty("property1", new JSType(), true, null);
    otherRecord.defineProperty("property1", new JSType(), true, null);
    
    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, null));
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper_differentProperties_false() {
    // Arrange
    RecordType otherRecord = new RecordType(null, null, true);
    recordType.defineProperty("property1", new JSType(), true, null);
    otherRecord.defineProperty("property2", new JSType(), true, null);
    
    assertFalse(recordType.checkRecordEquivalenceHelper(otherRecord, null));
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper_differentTypes_false() {
    // Arrange
    RecordType otherRecord = new RecordType(null, null, true);
    recordType.defineProperty("property1", new JSType(), true, null);
    otherRecord.defineProperty("property1", new JSType2(), true, null);
    
    assertFalse(recordType.checkRecordEquivalenceHelper(otherRecord, null));
  }
  
  @Test
  public void testGetPropertyType_existingProperty_typeReturned() {
    // Arrange
    JSType type = new JSType();
    recordType.defineProperty("property1", type, true, null);
    
    // Act
    JSType result = recordType.getPropertyType("property1");

    // Assert
    assertEquals(type, result);
  }
  
  @Test
  public void testGetPropertyType_nonExistingProperty_nullReturned() {
    // Arrange
    recordType.defineProperty("property1", new JSType(), true, null);
    
    // Act
    JSType result = recordType.getPropertyType("property2");

    // Assert
    assertEquals(null, result);
  }
  
  @Test
  public void testGetGreatestSubtypeHelper_subtypesExists_subtypeReturned() {
    // Arrange
    JSType that = new JSType();
    RecordType thatRecord = new RecordType(null, null, true);
    thatRecord.defineProperty("property1", new JSType(), true, null);
    
    recordType.defineProperty("property1", new JSType(), true, null);
    recordType.defineProperty("property2", new JSType(), true, null);
    
    // Act
    JSType result = recordType.getGreatestSubtypeHelper(that);

    // Assert
    assertEquals(that, result);
  }
  
  @Test
  public void testGetGreatestSubtypeHelper_noSubtypesExists_noObjectTypeReturned() {
    // Arrange
    JSType that = new JSType();
    RecordType thatRecord = new RecordType(null, null, true);
    thatRecord.defineProperty("property3", new JSType(), true, null);
    
    // Act
    JSType result = recordType.getGreatestSubtypeHelper(that);

    // Assert
    assertEquals(JSType.NO_TYPE, result);
  }
  
  @Test
  public void testIsSubtype_subtype_true() {
    // Arrange
    JSType that = new JSType();
    RecordType thatRecord = new RecordType(null, null, true);
    thatRecord.defineProperty("property1", new JSType(), true, null);
    
    recordType.defineProperty("property1", new JSType(), true, null);
    
    // Act
    boolean result = recordType.isSubtype(that);

    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testIsSubtype_notSubtype_false() {
    // Arrange
    JSType that = new JSType();
    RecordType thatRecord = new RecordType(null, null, true);
    thatRecord.defineProperty("property3", new JSType(), true, null);
    
    // Act
    boolean result = recordType.isSubtype(that);

    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testIsSubtype_otherTypeIsRecordType_true() {
    // Arrange
    JSType that = new RecordType(null, null, true);
    ((RecordType) that).defineProperty("property1", new JSType(), true, null);
    
    recordType.defineProperty("property1", new JSType(), true, null);
    
    // Act
    boolean result = recordType.isSubtype(that);

    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testIsSubtype_otherTypeIsNotRecordType_false() {
    // Arrange
    JSType that = new JSType();
    ((RecordType) that).defineProperty("property3", new JSType(), true, null);
    
    // Act
    boolean result = recordType.isSubtype(that);

    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testIsSubtype_otherTypeIsEquivalentRecordType_true() {
    // Arrange
    JSType that = new RecordType(null, null, true);
    that.defineProperty("property1", new JSType(), true, null);
    
    recordType.defineProperty("property1", new JSType(), true, null);
    
    // Act
    boolean result = recordType.isSubtype(that);

    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testIsSubtype_otherTypeIsEquivalentRecordType_false() {
    // Arrange
    JSType that = new RecordType(null, null, true);
    that.defineProperty("property1", new JSType(), true, null);
    
    // Act
    boolean result = recordType.isSubtype(that);

    // Assert
    assertFalse(result);
  }
}