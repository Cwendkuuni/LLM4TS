import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeWithEmptyCollection() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        
        Collection<Object> emptyCollection = new ArrayList<>();
        Assert.assertFalse(expression.compute(emptyCollection, emptyCollection));
    }
    
    @Test
    public void testComputeWithNullValues() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        
        Assert.assertFalse(expression.compute(null, null));
        Assert.assertFalse(expression.compute(null, "test"));
        Assert.assertFalse(expression.compute("test", null));
    }
    
    @Test
    public void testComputeWithDifferentObjectTypes() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        
        Iterator<Object> iterator1 = new ArrayList<Object>().iterator();
        Iterator<Object> iterator2 = new HashSet<Object>().iterator();
        
        Assert.assertFalse(expression.compute(iterator1, iterator2));
    }
    
    @Test
    public void testComputeWithIteratorContainingNullValues() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        
        Collection<Object> collection = new ArrayList<>();
        collection.add("test");
        collection.add(null);
        
        Iterator<Object> iterator = collection.iterator();
        
        Assert.assertTrue(expression.compute(iterator, null));
    }
}