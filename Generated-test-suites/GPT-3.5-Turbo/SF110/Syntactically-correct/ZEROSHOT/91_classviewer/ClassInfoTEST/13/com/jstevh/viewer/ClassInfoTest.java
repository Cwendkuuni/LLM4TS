import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {
  
  private ClassInfo classInfo;
  
  @Before
  public void setUp() throws ClassNotFoundException {
    classInfo = new ClassInfo();
  }
  
  @Test
  public void testIsAbstract() {
    assertFalse(classInfo.isAbstract());
  }
  
  @Test
  public void testIsInterface() {
    assertFalse(classInfo.isInterface());
  }
  
  @Test
  public void testGetClassName() {
    assertEquals("com.jstevh.viewer.ClassInfo", classInfo.getClassName());
  }
  
  @Test
  public void testGetClassPackage() {
    assertNull(classInfo.getClassPackage());
  }
  
  @Test
  public void testGetSuperClassName() {
    assertNull(classInfo.getSuperClassName());
  }
  
  @Test
  public void testPrintFields() {
    assertNull(classInfo.printFields());
  }
  
  @Test
  public void testPrintMethods() {
    assertNull(classInfo.printMethods());
  }
  
  @Test
  public void testPrintMethodsParam() {
    assertNull(classInfo.printMethods(1));
  }
  
  @Test
  public void testPrintConstructors() {
    assertNull(classInfo.printConstructors());
  }
  
  @Test
  public void testPrintInterfaces() {
    assertNull(classInfo.printInterfaces());
  }
  
  @Test
  public void testSrchMethods() {
    assertNull(classInfo.srchMethods("test"));
  }
  
  @Test
  public void testSrchMethodsParam() {
    assertNull(classInfo.srchMethods("test", new String[]{"index"}));
  }
  
  @Test
  public void testGetFoundMethod() {
    assertNull(classInfo.getFoundMethod(1));
  }
}