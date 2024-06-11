import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        // Creating attribute models for testing
        AttributeModel a = new AttributeModel("A");
        AttributeModel b = new AttributeModel("B");
        AttributeModel c = new AttributeModel("C");
        AttributeModel d = new AttributeModel("D");

        // Adding attribute models to the list
        ArrayList<AttributeModel> attributeModels = new ArrayList<>();
        attributeModels.add(a);
        attributeModels.add(b);
        attributeModels.add(c);
        attributeModels.add(d);

        // Sorting the list using the comparator
        Collections.sort(attributeModels, comparator);

        // Checking if the list is sorted correctly
        assertEquals(a, attributeModels.get(0));
        assertEquals(b, attributeModels.get(1));
        assertEquals(c, attributeModels.get(2));
        assertEquals(d, attributeModels.get(3));
    }
}