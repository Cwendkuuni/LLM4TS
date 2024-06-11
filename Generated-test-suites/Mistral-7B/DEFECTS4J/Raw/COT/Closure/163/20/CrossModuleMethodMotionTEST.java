import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.rhino.IR;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrossModuleMethodMotionTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    IdGenerator idGenerator = mock(IdGenerator.class);
    JSModule moduleGraph = mock(JSModule.class);
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);

    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);

    Node externRoot = Node.objectLiteral();
    Node root = Node.objectLiteral();

    crossModuleMethodMotion.process(externRoot, root);
  }

  @Test
  public void testMoveMethods() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    IdGenerator idGenerator = new IdGenerator();
    JSModule moduleGraph = mock(JSModule.class);
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);

    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);

    // Test with an empty collection and no name infos
    crossModuleMethodMotion.moveMethods(Collections.emptyList());

    // Test with a collection containing a NameInfo that does not have a deepestCommonModuleRef
    NameInfo nameInfo = mock(NameInfo.class);
    when(nameInfo.isReferenced()).thenReturn(true);
    when(nameInfo.readsClosureVariables()).thenReturn(false);
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(null);
    List<NameInfo> allNameInfo = new ArrayList<>();
    allNameInfo.add(nameInfo);

    crossModuleMethodMotion.moveMethods(allNameInfo);

    // Test with a collection containing a NameInfo that has a deepestCommonModuleRef
    nameInfo = mock(NameInfo.class);
    when(nameInfo.isReferenced()).thenReturn(true);
    when(nameInfo.readsClosureVariables()).thenReturn(false);
    JSModule deepestCommonModuleRef = mock(JSModule.class);
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(deepestCommonModuleRef);
    allNameInfo = new ArrayList<>();
    allNameInfo.add(nameInfo);

    crossModuleMethodMotion.moveMethods(allNameInfo);
  }

  @Test(expected = NullPointerException.class)
  public void testMoveMethodsWhenNullDeepestCommonModuleRef() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    IdGenerator idGenerator = mock(IdGenerator.class);
    JSModule moduleGraph = mock(JSModule.class);
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);

    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);

    NameInfo nameInfo = mock(NameInfo.class);
    when(nameInfo.isReferenced()).thenReturn(true);
    when(nameInfo.readsClosureVariables()).thenReturn(false);
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(null);
    List<NameInfo> allNameInfo = new ArrayList<>();
    allNameInfo.add(nameInfo);

    crossModuleMethodMotion.moveMethods(allNameInfo);
  }

  @Test
  public void testIdGenerator() {
    IdGenerator idGenerator = new IdGenerator();

    int id1 = idGenerator.newId();
    int id2 = idGenerator.newId();

    assertSame(id1, id2);

    int id3 = idGenerator.newId();
    assertNotSame(id1, id3);
  }
}