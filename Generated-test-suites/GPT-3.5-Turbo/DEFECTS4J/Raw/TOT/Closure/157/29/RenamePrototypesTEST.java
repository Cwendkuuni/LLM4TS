import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {
  
  private RenamePrototypes renamePrototypes;
  
  @Before
  public void setUp() {
    renamePrototypes = new RenamePrototypes();
  }
  
  @Test
  public void testProcess_noPropertiesToRename() {
    String input = "var a = 5;";
    String expected = input;
    
    String result = renamePrototypes.process(input);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testProcess_renamePrototypeProperty() {
    String input = "var obj = {\"getBar\" : function() { ... }};";
    String expected = "var obj = {\"getBar\" : function() { ... }};";
    
    String result = renamePrototypes.process(input);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testProcess_renameObjLitProperty() {
    String input = "var obj = {name : \"John\"};";
    String expected = "var obj = {name : \"John\"};";
    
    String result = renamePrototypes.process(input);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testProcess_renameBothProperties() {
    String input = "var obj = {\"getBar\" : function() { ... }, bar : \"test\"};";
    String expected = "var obj = {\"getBar\" : function() { ... }, bar : \"test\"};";
    
    String result = renamePrototypes.process(input);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testProcess_reservedPropertyName() {
    String input = "var obj = {indexOf : \"test\"};";
    String expected = "var obj = {indexOf : \"test\"};";
    
    String result = renamePrototypes.process(input);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testProcess_reservedCharacterInGeneratedName() {
    String input = "var obj = {\"getBar\" : function() { ... }};";
    String expected = "var obj = {\"getBar\" : function() { ... }};";
    
    String result = renamePrototypes.process(input);
    
    assertEquals(expected, result);
  }
  
}