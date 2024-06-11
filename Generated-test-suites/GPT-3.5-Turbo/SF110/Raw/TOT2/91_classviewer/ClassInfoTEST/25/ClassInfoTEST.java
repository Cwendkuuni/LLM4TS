import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
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
    public void testDataIsNullWhenNoPublicFields() {
        assertNull(classInfo.printFields());
    }

    @Test
    public void testDataIsNullWhenNoPublicMethods() {
        assertNull(classInfo.printMethods());
    }

    // Additional test cases for other public methods should be added here

}