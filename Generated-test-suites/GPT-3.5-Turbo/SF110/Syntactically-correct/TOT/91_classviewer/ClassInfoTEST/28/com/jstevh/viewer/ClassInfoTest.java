import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

  private ClassInfo classInfo;

  @Before
  public void setUp() {
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
    String[] fields = classInfo.printFields();
    assertNotNull(fields);
    assertEquals(0, fields.length);
  }

  @Test
  public void testPrintMethods() {
    String[] methods = classInfo.printMethods();
    assertNotNull(methods);
    assertEquals(26, methods.length);
  }

  @Test
  public void testPrintMethodsParam() {
    String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
    assertNotNull(methods);
    assertEquals(26, methods.length);
  }

  @Test
  public void testPrintConstructors() {
    String[] constructors = classInfo.printConstructors();
    assertNotNull(constructors);
    assertEquals(2, constructors.length);
  }

  @Test
  public void testPrintInterfaces() {
    String[] interfaces = classInfo.printInterfaces();
    assertNotNull(interfaces);
    assertEquals(0, interfaces.length);
  }

  @Test
  public void testGetDataMember() {
    String[] data = classInfo.getData(new Member[0]);
    assertNull(data);
  }

  @Test
  public void testGetDataClass() {
    String[] data = classInfo.getData(new Class[0]);
    assertNull(data);
  }

  @Test
  public void testSrchMethods() {
    String[] methods = classInfo.srchMethods(null);
    assertNull(methods);
  }

  @Test
  public void testSrchMethodsIndex() {
    String[] methods = classInfo.srchMethods(null, null);
    assertNull(methods);
  }

  @Test
  public void testGetFoundMethod() {
    MethodData methodData = classInfo.getFoundMethod(0);
    assertNotNull(methodData);
    assertNull(methodData.getMethPackage());
    assertEquals("com.jstevh.viewer.ClassInfo", methodData.getMethClass());
    assertEquals("isAbstract", methodData.getMethName());
  }
}