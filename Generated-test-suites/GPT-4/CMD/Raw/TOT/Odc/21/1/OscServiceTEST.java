import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Optional<DatabaseEntity> dbOptional = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(anyLong())).thenReturn(dbOptional);
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertEquals(dbOptional.get().getDatabaseId(), oscDatabase.getDatabaseId());
    }

    @Test
    public void testGetOscDatabaseInfo_NotFound() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertNull(oscDatabase.getDatabaseId());
    }

   // More tests for other methods...
}