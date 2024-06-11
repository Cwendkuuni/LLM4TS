
import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        // Test when o1 and o2 are null
        int nullComparison = comparator.compare(null, null);
        assertEquals(0, nullComparison);

        // Test when o1 is null and o2 is not null
        AttributeModel o2 = new AttributeModel();
        int nullO1Comparison = comparator.compare(null, o2);
        assertEquals(-1, nullO1Comparison);

        // Test when o1 is not null and o2 is null
        AttributeModel o1 = new AttributeModel();
        int nullO2Comparison = comparator.compare(o1, null);
        assertEquals(1, nullO2Comparison);

        // Test when o1 and o2 have the same name
        o1.setName("name");
        o2.setName("name");
        int sameNameComparison = comparator.compare(o1, o2);
        assertEquals(0, sameNameComparison);

        // Test when o1 comes before "a" and o2 comes after "a"
        o1.setName("aa");
        o2.setName("b");
        int o1BeforeAComparison = comparator.compare(o1, o2);
        assertEquals(-1, o1BeforeAComparison);

        // Test when o1 comes after "a" and o2 comes before "a"
        o1.setName("b");
        o2.setName("aa");
        int o1AfterAComparison = comparator.compare(o1, o2);
        assertEquals(1, o1AfterAComparison);

        // Test when o1 and o2 come before "a"
        o1.setName("aa");
        o2.setName("aa");
        int bothBeforeAComparison = comparator.compare(o1, o2);
        assertEquals(0, bothBeforeAComparison);

        // Test when o1 and o2 come after "a"
        o1.setName("b");
        o2.setName("b");
        int bothAfterAComparison = comparator.compare(o1, o2);
        assertEquals(0, bothAfterAComparison);

        // Test when o1 and o2 have empty names
        o1.setName("");
        o2.setName("");
        int emptyNameComparison = comparator.compare(o1, o2);
        assertEquals(0, emptyNameComparison);
    }
}

