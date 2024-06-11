import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.ArrayList;
import java.util.List;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;

    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
    }

    @Test
    public void testCompare_BothNull_ReturnsZero() {
        // Arrange
        AttributeModel o1 = null;
        AttributeModel o2 = null;

        // Act
        int result = comparator.compare(o1, o2);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testCompare_OneNull_ReturnsNegativeValue() {
        // Arrange
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel("Name");

        // Act
        int result = comparator.compare(o1, o2);

        // Assert
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_BothNotNull_NameComparison_ReturnsNegativeValue() {
        // Arrange
        AttributeModel o1 = new AttributeModel("Name1");
        AttributeModel o2 = new AttributeModel("Name2");

        // Act
        int result = comparator.compare(o1, o2);

        // Assert
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_BothNotNull_NameComparison_ReturnsPositiveValue() {
        // Arrange
        AttributeModel o1 = new AttributeModel("Name2");
        AttributeModel o2 = new AttributeModel("Name1");

        // Act
        int result = comparator.compare(o1, o2);

        // Assert
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_BothNotNull_NameComparison_ReturnsZero() {
        // Arrange
        AttributeModel o1 = new AttributeModel("Name");
        AttributeModel o2 = new AttributeModel("Name");

        // Act
        int result = comparator.compare(o1, o2);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testCompare_BothNotNull_NameComparison_A_Inside() {
        // Arrange
        AttributeModel o1 = new AttributeModel("ZName");
        AttributeModel o2 = new AttributeModel("aName");

        // Act
        int result = comparator.compare(o1, o2);

        // Assert
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_BothNotNull_NameComparison_A_Outside() {
        // Arrange
        AttributeModel o1 = new AttributeModel("aName");
        AttributeModel o2 = new AttributeModel("ZName");

        // Act
        int result = comparator.compare(o1, o2);

        // Assert
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_BothNotNull_NameComparison_A_Inside_CaseInsensitive() {
        // Arrange
        AttributeModel o1 = new AttributeModel("zname");
        AttributeModel o2 = new AttributeModel("Aname");

        // Act
        int result = comparator.compare(o1, o2);

        // Assert
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_BothNotNull_NameComparison_A_Outside_CaseInsensitive() {
        // Arrange
        AttributeModel o1 = new AttributeModel("Aname");
        AttributeModel o2 = new AttributeModel("Zname");

        // Act
        int result = comparator.compare(o1, o2);

        // Assert
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_BothNotNull_NameComparison_A_Inside_EqualsA() {
        // Arrange
        AttributeModel o1 = new AttributeModel("A");
        AttributeModel o2 = new AttributeModel("a");

        // Act
        int result = comparator.compare(o1, o2);

        // Assert
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_BothNotNull_NameComparison_A_Outside_EqualsA() {
        // Arrange
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("A");

        // Act
        int result = comparator.compare(o1, o2);

        // Assert
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_EqualsA_SpecialCharacters() {
        // Arrange
        AttributeModel o1 = new AttributeModel("@@@@");
        AttributeModel o2 = new AttributeModel("a");

        // Act
        int result = comparator.compare(o1, o2);

        // Assert
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_EqualsA_Number() {
        // Arrange
        AttributeModel o1 = new AttributeModel("123");
        AttributeModel o2 = new AttributeModel("a");

        // Act
        int result = comparator.compare(o1, o2);

        // Assert
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_EqualsA_EmptyString() {
        // Arrange
        AttributeModel attributeModel1 = new AttributeModel("");
        AttributeModel attributeModel2 = new AttributeModel("a");

        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);

        // Assert
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_EqualsA_NullString() {
        // Arrange
        AttributeModel attributeModel1 = new AttributeModel(null);
        AttributeModel attributeModel2 = new AttributeModel("a");

        // Act
        int result = comparator.compare(attributeModel1, attributeModel2);

        // Assert
        assertTrue(result < 0);
    }
}