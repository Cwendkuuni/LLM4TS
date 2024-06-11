import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionTest {

 private Expression expression;
 @Mock
 private EvalContext context;

 @Before
 public void setUp() {
 expression = Mockito.spy(new Expression() {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return null;
 }

 @Override
 public Object compute(EvalContext context) {
 return null;
 }
 });
 }

 @Test
 public void testIsContextDependent() {
 assertFalse(expression.isContextDependent());
 }

 @Test
 public void testIterate() {
 Iterator iterator = expression.iterate(context);
 assertNotNull(iterator);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratePointers() {
 Iterator iterator = expression.iteratePointers(context);
 assertNotNull(iterator);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testComputeValue() {
 assertNull(expression.computeValue(context));
 }

 @Test
 public void testCompute() {
 assertNull(expression.compute(context));
 }
}