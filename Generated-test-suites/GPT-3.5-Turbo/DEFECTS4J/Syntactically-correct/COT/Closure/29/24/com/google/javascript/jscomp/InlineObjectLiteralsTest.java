import org.junit.Assert;
import org.junit.Test;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Set;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.base.Preconditions;

public class InlineObjectLiteralsTest {

  @Test
  public void testIsVarInlineForbidden() {
    InlineObjectLiterals obj = new InlineObjectLiterals();
    boolean result = obj.isVarInlineForbidden(var);
    Assert.assertEquals(false, result);
  }

  @Test
  public void testIsInlinableObject() {
    InlineObjectLiterals obj = new InlineObjectLiterals();
    boolean result = obj.isInlinableObject(refs);
    Assert.assertEquals(false, result);
  }

  @Test
  public void testComputeVarList() {
    InlineObjectLiterals obj = new InlineObjectLiterals();
    Map<String, String> result = obj.computeVarList(v, referenceInfo);
    Assert.assertEquals(expectedMap, result);
  }

  @Test
  public void testFillInitialValues() {
    InlineObjectLiterals obj = new InlineObjectLiterals();
    obj.fillInitialValues(init, initvals);
    Assert.assertEquals(expectedMap, initvals);
  }

  @Test
  public void testReplaceAssignmentExpression() {
    InlineObjectLiterals obj = new InlineObjectLiterals();
    obj.replaceAssignmentExpression(v, ref, varmap);
    Assert.assertEquals(expectedReplacement, replacement);
  }

  @Test
  public void testSplitObject() {
    InlineObjectLiterals obj = new InlineObjectLiterals();
    obj.splitObject(v, declaration, init, referenceInfo);
    Assert.assertEquals(expectedNode, vnode);
  }

}