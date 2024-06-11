import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {
  
  private VarCheck varCheck;
  private AbstractCompiler compiler;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    varCheck = new VarCheck(compiler);
  }
  
  // Test for process() method
  @Test
  public void testProcess() {
    // TODO: implement test case
  }
  
  // Test for visit() method
  @Test
  public void testVisit() {
    // TODO: implement test cases for different scenarios
  }
  
  // Test for createSynthesizedExternVar() method
  @Test
  public void testCreateSynthesizedExternVar() {
    // TODO: implement test case
  }
  
  // Test for getSynthesizedExternsInput() method
  @Test
  public void testGetSynthesizedExternsInput() {
    // TODO: implement test case
  }
  
  // Test for getSynthesizedExternsRoot() method
  @Test
  public void testGetSynthesizedExternsRoot() {
    // TODO: implement test case
  }
}