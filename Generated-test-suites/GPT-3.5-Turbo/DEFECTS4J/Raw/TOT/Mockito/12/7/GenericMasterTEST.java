import org.junit.*;
import org.mockito.*;
import org.mockito.internal.util.reflection.GenericMaster;
import java.lang.reflect.Field;


public class GenericMasterTest {

    private GenericMaster genericMaster;
    private Field genericField;

    @Before
    public void setUp() throws NoSuchFieldException{
        genericMaster = new GenericMaster();
        genericField = GenericMaster.class.getDeclaredField("generic");
    }

    @Test
    public void testGetGenericType_WhenGenericFieldExists_ExpectActualGenericType(){

        //Arrange
        Class expected = String.class;

        //Act
        Class actual = genericMaster.getGenericType(genericField);

        //Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericType_WhenNoGenericField_ExpectObjectClass(){

        //Arrange
        Class expected = Object.class;
        Field nonGenericField = GenericMaster.class.getDeclaredField("nonGeneric");

        //Act
        Class actual = genericMaster.getGenericType(nonGenericField);

        //Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericType_WhenNestedGenericFieldExists_ExpectActualGenericType(){

        //Arrange
        Class expected = Integer.class;
        Field nestedGenericField = GenericMaster.class.getDeclaredField("nestedGeneric");

        //Act
        Class actual = genericMaster.getGenericType(nestedGenericField);

        //Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericType_WhenFieldIsNull_ExpectObjectClass(){

        //Arrange
        Class expected = Object.class;

        //Act
        Class actual = genericMaster.getGenericType(null);

        //Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericType_WhenGenericTypeIsNull_ExpectObjectClass(){

        //Arrange
        Class expected = Object.class;
        Field nullGenericTypeField = GenericMaster.class.getDeclaredField("nullGenericType");

        //Act
        Class actual = genericMaster.getGenericType(nullGenericTypeField);

        //Assert
        Assert.assertEquals(expected, actual);
    }

}