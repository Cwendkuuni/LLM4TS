import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import com.jstevh.tools.DirManager;

public class ClassInfoTest {
  
  @Test
  public void testIsAbstract() {
    ClassInfo classInfo = new ClassInfo();
    assertFalse(classInfo.isAbstract());
  }
  
  @Test
  public void testIsInterface() {
    ClassInfo classInfo = new ClassInfo();
    assertFalse(classInfo.isInterface());
  }
  
  @Test
  public void testGetClassName() {
    ClassInfo classInfo = new ClassInfo();
    assertEquals("javax.swing.WindowConstants", classInfo.getClassName());
  }
  
  @Test
  public void testGetClassPackage() {
    ClassInfo classInfo = new ClassInfo();
    assertNull(classInfo.getClassPackage());
  }
  
  @Test
  public void testGetSuperClassName() {
    ClassInfo classInfo = new ClassInfo();
    assertNull(classInfo.getSuperClassName());
  }
  
  @Test
  public void testPrintFields() {
    ClassInfo classInfo = new ClassInfo();
    assertNull(classInfo.printFields());
  }
  
  @Test
  public void testPrintMethods() {
    ClassInfo classInfo = new ClassInfo();
    assertNull(classInfo.printMethods());
  }
  
  @Test
  public void testPrintConstructors() {
    ClassInfo classInfo = new ClassInfo();
    assertNull(classInfo.printConstructors());
  }
  
  @Test
  public void testPrintInterfaces() {
    ClassInfo classInfo = new ClassInfo();
    assertNull(classInfo.printInterfaces());
  }
  
  @Test
  public void testGetData() {
    Field[] fields = ClassInfo.class.getDeclaredFields();
    String[] data = ClassInfo.getData(fields);
    assertNotNull(data);
    assertEquals(fields.length, data.length);
  }
  
  @Test
  public void testTryClass() {
    Class clazz = ClassInfo.tryClass("java.lang.String");
    assertNotNull(clazz);
  }
}