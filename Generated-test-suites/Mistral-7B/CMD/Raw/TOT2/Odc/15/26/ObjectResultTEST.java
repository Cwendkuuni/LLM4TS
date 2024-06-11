import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectResultTest {

    // 
    @Test
    public void testObjectResultEmptyConstructor() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getExportPaths());
        assertNull(objectResult.getSchema());
        assertNull(objectResult.getName());
        assertNull(objectResult.getType());
        assertNull(objectResult.getStatus());
        assertNull(objectResult.getCount());
        assertNull(objectResult.getTotal());
        assertThat(objectResult.getSummary()).isNull();
    }

    // 
}