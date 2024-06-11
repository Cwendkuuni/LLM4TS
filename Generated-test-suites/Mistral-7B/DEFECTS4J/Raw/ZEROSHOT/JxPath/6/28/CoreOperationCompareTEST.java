import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

 private Expression arg1;
 private Expression arg2;
 private CoreOperationCompare coreOperationCompare;
 private EvalContext context;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 arg1 = Mockito.mock(Expression.class);
 arg2 = Mockito.mock(Expression.class);
 context = Mockito.mock(EvalContext.class);
 coreOperationCompare = new CoreOperationCompare(arg1, arg2);
 }

 @Test
 public void testEqualWithNullValues() {
 Mockito.when(arg1.compute(context)).thenReturn(null);
 Mockito.when(arg2.compute(context)).thenReturn(null);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithSameObjects() {
 Mockito.when(arg1.compute(context)).thenReturn(new Object());
 Mockito.when(arg2.compute(context)).thenReturn(new Object());
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithDifferentObjects() {
 Mockito.when(arg1.compute(context)).thenReturn("first");
 Mockito.when(arg2.compute(context)).thenReturn("second");
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithSameIterators() {
 Collection<Object> collection = new HashSet<>();
 collection.add("first");
 collection.add("second");
 Mockito.when(arg1.compute(context)).thenReturn(collection.iterator());
 Mockito.when(arg2.compute(context)).thenReturn(collection.iterator());
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithDifferentIteratorsContainingSameElements() {
 Collection<Object> collection1 = new HashSet<>();
 collection1.add("first");
 collection1.add("second");
 Collection<Object> collection2 = new HashSet<>();
 collection2.add("second");
 collection2.add("third");
 Mockito.when(arg1.compute(context)).thenReturn(collection1.iterator());
 Mockito.when(arg2.compute(context)).thenReturn(collection2.iterator());
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithDifferentIterators() {
 Collection<Object> collection1 = new HashSet<>();
 collection1.add("first");
 Mockito.when(arg1.compute(context)).thenReturn(collection1.iterator());
 Collection<Object> collection2 = new HashSet<>();
 collection2.add("second");
 Mockito.when(arg2.compute(context)).thenReturn(collection2.iterator());
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithEmptyIterators() {
 Iterator<Object> iterator1 = Mockito.mock(Iterator.class);
 Mockito.when(arg1.compute(context)).thenReturn(iterator1);
 Iterator<Object> iterator2 = Mockito.mock(Iterator.class);
 Mockito.when(arg2.compute(context)).thenReturn(iterator2);
 Mockito.when(iterator1.hasNext()).thenReturn(false);
 Mockito.when(iterator2.hasNext()).thenReturn(false);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithNonEmptyIterators() {
 Collection<Object> collection1 = new HashSet<>();
 collection1.add("first");
 Iterator<Object> iterator1 = collection1.iterator();
 Collection<Object> collection2 = new HashSet<>();
 collection2.add("second");
 Iterator<Object> iterator2 = collection2.iterator();
 Mockito.when(arg1.compute(context)).thenReturn(iterator1);
 Mockito.when(arg2.compute(context)).thenReturn(iterator2);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithSelfContexts() {
 InitialContext initialContext = new InitialContext();
 Mockito.when(arg1.compute(context)).thenReturn(initialContext);
 Mockito.when(arg2.compute(context)).thenReturn(initialContext);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithSelfContextAndDifferentObjects() {
 InitialContext initialContext = new InitialContext();
 Pointer pointer = new Pointer("node");
 Mockito.when(arg1.compute(context)).thenReturn(initialContext);
 Mockito.when(arg2.compute(context)).thenReturn(pointer);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithSelfContextAndSameObjects() {
 InitialContext initialContext = new InitialContext();
 Pointer pointer = new Pointer("node");
 Mockito.when(arg1.compute(context)).thenReturn(initialContext);
 Mockito.when(arg2.compute(context)).thenReturn(pointer);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithInitialContextAndDifferentObjects() {
 InitialContext initialContext1 = new InitialContext();
 InitialContext initialContext2 = new InitialContext();
 Mockito.when(arg1.compute(context)).thenReturn(initialContext1);
 Mockito.when(arg2.compute(context)).thenReturn(initialContext2);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithInitialContextAndSameObjects() {
 InitialContext initialContext1 = new InitialContext();
 InitialContext initialContext2 = new InitialContext();
 Mockito.when(arg1.compute(context)).thenReturn(initialContext1);
 Mockito.when(arg2.compute(context)).thenReturn(initialContext2);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithSelfContextAndCollection() {
 InitialContext initialContext = new InitialContext();
 Collection<Object> collection = new HashSet<>();
 collection.add("node");
 Mockito.when(arg1.compute(context)).thenReturn(initialContext);
 Mockito.when(arg2.compute(context)).thenReturn(collection.iterator());
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithCollectionAndSelfContext() {
 Collection<Object> collection = new HashSet<>();
 collection.add("node");
 Iterator<Object> iterator = collection.iterator();
 Mockito.when(arg1.compute(context)).thenReturn(iterator);
 Mockito.when(arg2.compute(context)).thenReturn(new InitialContext());
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithCollectionAndCollection() {
 Collection<Object> collection1 = new HashSet<>();
 collection1.add("first");
 collection1.add("second");
 Collection<Object> collection2 = new HashSet<>();
 collection2.add("first");
 collection2.add("second");
 Mockito.when(arg1.compute(context)).thenReturn(collection1.iterator());
 Mockito.when(arg2.compute(context)).thenReturn(collection2.iterator());
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithCollectionsContainingDifferentElements() {
 Collection<Object> collection1 = new HashSet<>();
 collection1.add("first");
 Collection<Object> collection2 = new HashSet<>();
 collection2.add("second");
 Mockito.when(arg1.compute(context)).thenReturn(collection1.iterator());
 Mockito.when(arg2.compute(context)).thenReturn(collection2.iterator());
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithCollectionsContainingSameElementsInDifferentOrder() {
 Collection<Object> collection1 = new HashSet<>();
 collection1.add("second");
 collection1.add("first");
 Collection<Object> collection2 = new HashSet<>();
 collection2.add("first");
 collection2.add("second");
 Mockito.when(arg1.compute(context)).thenReturn(collection1.iterator());
 Mockito.when(arg2.compute(context)).thenReturn(collection2.iterator());
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithCollectionsContainingSameElementsButDifferentTypes() {
 Collection<Object> collection1 = new HashSet<>();
 collection1.add("first");
 collection1.add(1);
 Collection<Object> collection2 = new HashSet<>();
 collection2.add("first");
 collection2.add(new Integer(1));
 Mockito.when(arg1.compute(context)).thenReturn(collection1.iterator());
 Mockito.when(arg2.compute(context)).thenReturn(collection2.iterator());
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithCollectionsContainingNullValues() {
 Collection<Object> collection1 = new HashSet<>();
 collection1.add("first");
 collection1.add(null);
 Collection<Object> collection2 = new HashSet<>();
 collection2.add("second");
 collection2.add(null);
 Mockito.when(arg1.compute(context)).thenReturn(collection1.iterator());
 Mockito.when(arg2.compute(context)).thenReturn(collection2.iterator());
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testContainsWithEmptyIteratorAndValue() {
 Iterator<Object> emptyIterator = Mockito.mock(Iterator.class);
 Mockito.when(arg1.compute(context)).thenReturn(emptyIterator);
 Mockito.when(arg2.compute(context)).thenReturn("value");
 assertFalse(coreOperationCompare.contains(emptyIterator, "value"));
 }

 @Test
 public void testContainsWithNonEmptyIteratorAndValue() {
 Collection<Object> collection = new HashSet<>();
 collection.add("first");
 Iterator<Object> iterator = collection.iterator();
 Mockito.when(arg1.compute(context)).thenReturn(iterator);
 Mockito.when(arg2.compute(context)).thenReturn("first");
 assertTrue(coreOperationCompare.contains(iterator, "first"));
 }

 @Test
 public void testFindMatchWithEmptyIterators() {
 Iterator<Object> iterator1 = Mockito.mock(Iterator.class);
 Mockito.when(arg1.compute(context)).thenReturn(iterator1);
 Iterator<Object> iterator2 = Mockito.mock(Iterator.class);
 Mockito.when(arg2.compute(context)).thenReturn(iterator2);
 Mockito.when(iterator1.hasNext()).thenReturn(false);
 Mockito.when(iterator2.hasNext()).thenReturn(false);
 assertTrue(coreOperationCompare.findMatch(iterator1, iterator2));
 }

 @Test
 public void testFindMatchWithNonEmptyIterators() {
 Collection<Object> collection1 = new HashSet<>();
 collection1.add("first");
 Iterator<Object> iterator1 = collection1.iterator();
 Collection<Object> collection2 = new HashSet<>();
 collection2.add("second");
 collection2.add("first");
 Iterator<Object> iterator2 = collection2.iterator();
 Mockito.when(arg1.compute(context)).thenReturn(iterator1);
 Mockito.when(arg2.compute(context)).thenReturn(iterator2);
 assertTrue(coreOperationCompare.findMatch(iterator1, iterator2));
 }
}