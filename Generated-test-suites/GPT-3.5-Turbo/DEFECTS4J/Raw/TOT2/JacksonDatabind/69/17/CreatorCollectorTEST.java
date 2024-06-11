import static org.junit.Assert.*;
import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creator = new CreatorCollector(null, null);
        AnnotatedWithParams mockCreator = new AnnotatedWithParamsMock();
        creator.setDefaultCreator(mockCreator);
        assertEquals(mockCreator, creator._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creator = new CreatorCollector(null, null);
        AnnotatedWithParams mockCreator = new AnnotatedWithParamsMock();
        creator.addStringCreator(mockCreator, true);
        assertEquals(mockCreator, creator._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector creator = new CreatorCollector(null, null);
        AnnotatedWithParams mockCreator = new AnnotatedWithParamsMock();
        creator.addIntCreator(mockCreator, false);
        assertEquals(mockCreator, creator._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector creator = new CreatorCollector(null, null);
        AnnotatedWithParams mockCreator = new AnnotatedWithParamsMock();
        creator.addLongCreator(mockCreator, true);
        assertEquals(mockCreator, creator._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creator = new CreatorCollector(null, null);
        AnnotatedWithParams mockCreator = new AnnotatedWithParamsMock();
        creator.addDoubleCreator(mockCreator, false);
        assertEquals(mockCreator, creator._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creator = new CreatorCollector(null, null);
        AnnotatedWithParams mockCreator = new AnnotatedWithParamsMock();
        creator.addBooleanCreator(mockCreator, true);
        assertEquals(mockCreator, creator._creators[CreatorCollector.C_BOOLEAN]);
    }

    // Add more test cases for other methods as needed

}