
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.rhino.Node;

public class CrossModuleMethodMotionTest {

  @Test
  public void process_withMultipleModules_shouldMoveMethods() {
    //create mock objects
    AbstractCompiler compiler = createMock(AbstractCompiler.class);
    IdGenerator idGenerator = createMock(IdGenerator.class);
    AnalyzePrototypeProperties analyzer = createMock(AnalyzePrototypeProperties.class);
    JSModuleGraph moduleGraph = createMock(JSModuleGraph.class);

    //expectations
    expect(moduleGraph.getModuleCount()).andReturn(2);
    expect(analyzer.getAllNameInfo()).andReturn(new HashSet<NameInfo>());

    //replay
    replay(compiler, idGenerator, analyzer, moduleGraph);

    //create instance of CrossModuleMethodMotion
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, false);

    //invoke process method
    crossModuleMethodMotion.process(new Node(), new Node());

    //verify
    verify(compiler, idGenerator, analyzer, moduleGraph);
  }

  @Test
  public void moveMethods_withNonReferencedNameInfos_shouldNotMoveMethods() {
    //create mock objects
    NameInfo nameInfo1 = createMock(NameInfo.class);
    NameInfo nameInfo2 = createMock(NameInfo.class);
    Collection<NameInfo> allNameInfo = Arrays.asList(nameInfo1, nameInfo2);

    //expectations
    expect(nameInfo1.isReferenced()).andReturn(false);
    expect(nameInfo2.isReferenced()).andReturn(false);

    //replay
    replay(nameInfo1, nameInfo2);

    //create instance of CrossModuleMethodMotion
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(null, null, false);

    //invoke moveMethods method
    crossModuleMethodMotion.moveMethods(allNameInfo);

    //verify
    verify(nameInfo1, nameInfo2);
  }
}
