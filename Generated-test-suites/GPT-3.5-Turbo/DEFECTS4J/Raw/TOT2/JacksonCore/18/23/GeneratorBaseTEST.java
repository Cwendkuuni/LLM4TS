import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeneratorBaseTest {

    @Test
    public void testGetVersion() {
        GeneratorBase generator = new GeneratorBaseImpl();
        assertEquals(VersionUtil.versionFor(generator.getClass()), generator.version());
    }

    private class GeneratorBaseImpl extends GeneratorBase {
        protected GeneratorBaseImpl() {
            super(0, null);
        }

        @Override
        protected void _releaseBuffers() { }

        @Override
        protected void _verifyValueWrite(String typeMsg) throws IOException { }
    }
}