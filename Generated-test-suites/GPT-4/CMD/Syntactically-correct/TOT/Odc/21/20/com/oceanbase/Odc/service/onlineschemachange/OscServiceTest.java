package com.oceanbase.odc.service.onlineschemachange;
// Import necessary testing and mock libraries
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

// 
@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private DatabaseRepository mockDatabaseRepository;

    @Mock
    private ConnectionService mockConnectionService;

    @InjectMocks
    private OscService oscServiceUnderTest;

    private DatabaseEntity mockDatabaseEntity;

    @Before
    public void setUp() {
        mockDatabaseEntity = new DatabaseEntity();
        mockDatabaseEntity.setDatabaseId(1L);
        mockDatabaseEntity.setConnectionId(2L);
    }

    @Test
    public void whenGetOscDatabaseInfo_withExistDatabase_thenReturnExpectedOscDatabaseInfo() {
        when(mockDatabaseRepository.findById(1L)).thenReturn(Optional.of(mockDatabaseEntity));
        when(mockConnectionService.getForConnectionSkipPermissionCheck(2L)).thenReturn(null);

        OscLockDatabaseUserInfo oscDatabase = oscServiceUnderTest.getOscDatabaseInfo(1L);

        assertThat(oscDatabase.getDatabaseId()).isEqualTo(mockDatabaseEntity.getDatabaseId());
        assertThat(oscDatabase.getLockDatabaseUserRequired()).isEqualTo(false);
    }

    @Test
    public void whenGetOscDatabaseInfo_withNotExistDatabase_thenReturnEmptyOscDatabaseInfo() {
        when(mockDatabaseRepository.findById(1L)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabase = oscServiceUnderTest.getOscDatabaseInfo(1L);

        assertThat(oscDatabase.getDatabaseId()).isEqualTo(null);
        assertThat(oscDatabase.getLockDatabaseUserRequired()).isEqualTo(false);
    }
}
//  