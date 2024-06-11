import static org.junit.Assert.*;
import org.junit.Test;
import com.jstevh.viewer.ClassInfo;

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
        assertEquals("com.jstevh.viewer.ClassInfo", classInfo.getClassName());
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
    public void testConstructorWithClassName() throws ClassNotFoundException {
        String name = "java.lang.String";
        ClassInfo classInfo = new ClassInfo(name);
        assertFalse(classInfo.isInterface());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithInvalidClassName() throws ClassNotFoundException {
        String name = "InvalidClassName";
        ClassInfo classInfo = new ClassInfo(name);
    }
}

