import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    SAXDirParserGetWebDataTest.class,
    SAXDirParserStartElementTest.class,
    SAXDirParserCharactersTest.class
})
public class SAXDirParserTest {
    // This class remains empty, and is used only as a holder for the above annotations
}