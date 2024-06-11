import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

  private CSVRecord csvRecord;
  private String[] values = {"Value1", "Value2", "Value3"};
  private Map<String, Integer> mapping;
  private String comment;
  private long recordNumber;
  private CSVFormat csvFormat;
  
  @Before
  public void setUp() {
    mapping = new HashMap<String, Integer>();
    mapping.put("Column1", 0);
    mapping.put("Column2", 1);
    mapping.put("Column3", 2);
    comment = "This is a comment";
    recordNumber = 1;
    csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
  }
  
  @Test
  public void testGetByEnum_ValidEnum_ReturnsValue() {
    assertEquals("Value1", csvRecord.get(Enum.COLUMN1));
    assertEquals("Value2", csvRecord.get(Enum.COLUMN2));
    assertEquals("Value3", csvRecord.get(Enum.COLUMN3));
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testGetByEnum_InvalidEnum_ThrowsIllegalArgumentException() {
    csvRecord.get(Enum.UNKNOWN_COLUMN);
  }
  
  @Test
  public void testGetByIndex_ValidIndex_ReturnsValue() {
    assertEquals("Value1", csvRecord.get(0));
    assertEquals("Value2", csvRecord.get(1));
    assertEquals("Value3", csvRecord.get(2));
  }
  
  @Test(expected=ArrayIndexOutOfBoundsException.class)
  public void testGetByIndex_InvalidIndex_ThrowsArrayIndexOutOfBoundsException() {
    csvRecord.get(3);
  }
  
  @Test
  public void testGetByName_ValidName_ReturnsValue() {
    assertEquals("Value1", csvRecord.get("Column1"));
    assertEquals("Value2", csvRecord.get("Column2"));
    assertEquals("Value3", csvRecord.get("Column3"));
  }
  
  @Test(expected=IllegalStateException.class)
  public void testGetByName_NoMappingProvided_ThrowsIllegalStateException() {
    CSVRecord csvRecordNoMapping = new CSVRecord(values, null, comment, recordNumber);
    csvRecordNoMapping.get("Column1");
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testGetByName_InvalidName_ThrowsIllegalArgumentException() {
    csvRecord.get("UnknownColumn");
  }
  
  @Test
  public void testIsConsistent_ValidMappingAndValues_ReturnsTrue() {
    assertTrue(csvRecord.isConsistent());
  }
  
  @Test
  public void testIsConsistent_ValidMappingAndValues_MismatchedSize_ReturnsFalse() {
    mapping.put("Column4", 3);
    assertFalse(csvRecord.isConsistent());
  }
  
  @Test
  public void testGetComment_CommentExists_ReturnsComment() {
    assertEquals("This is a comment", csvRecord.getComment());
  }
  
  @Test
  public void testGetComment_NoComment_ReturnsNull() {
    CSVRecord csvRecordNoComment = new CSVRecord(values, mapping, null, recordNumber);
    assertNull(csvRecordNoComment.getComment());
  }
  
  @Test
  public void testGetRecordNumber_ReturnsRecordNumber() {
    assertEquals(1, csvRecord.getRecordNumber());
  }
  
  @Test
  public void testIsMapped_MappedColumnName_ReturnsTrue() {
    assertTrue(csvRecord.isMapped("Column1"));
  }
  
  @Test
  public void testIsMapped_UnmappedColumnName_ReturnsFalse() {
    assertFalse(csvRecord.isMapped("UnknownColumn"));
  }
  
  @Test
  public void testIsSet_MappedColumnNameWithValidIndex_ReturnsTrue() {
    assertTrue(csvRecord.isSet("Column1"));
  }
  
  @Test
  public void testIsSet_MappedColumnNameWithInvalidIndex_ReturnsFalse() {
    mapping.put("Column4", 3);
    assertFalse(csvRecord.isSet("Column4"));
  }
  
  @Test
  public void testIterator_ReturnsIteratorWithValues() {
    List<String> expectedList = Arrays.asList("Value1", "Value2", "Value3");
    Iterator<String> iterator = csvRecord.iterator();
    while(iterator.hasNext()) {
      assertEquals(expectedList.get(iterator.nextIndex()), iterator.next());
    }
  }
  
  @Test
  public void testPutIn_ReturnsPopulatedMap() {
    Map<String, String> expectedMap = new HashMap<String, String>();
    expectedMap.put("Column1", "Value1");
    expectedMap.put("Column2", "Value2");
    expectedMap.put("Column3", "Value3");
    Map<String, String> resultMap = csvRecord.putIn(new HashMap<String, String>());
    assertEquals(expectedMap, resultMap);
  }
  
  @Test
  public void testSize_ReturnsNumberOfValues() {
    assertEquals(3, csvRecord.size());
  }
  
  @Test
  public void testToMap_ReturnsPopulatedMap() {
    Map<String, String> expectedMap = new HashMap<String, String>();
    expectedMap.put("Column1", "Value1");
    expectedMap.put("Column2", "Value2");
    expectedMap.put("Column3", "Value3");
    Map<String, String> resultMap = csvRecord.toMap();
    assertEquals(expectedMap, resultMap);
  }
  
  @Test
  public void testToString_ReturnsStringRepresentationOfValues() {
    assertEquals("[Value1, Value2, Value3]", csvRecord.toString());
  }
  
  @Test
  public void testValues_ReturnsValuesArray() {
    assertArrayEquals(values, csvRecord.values());
  }
}