package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ConnectionService connectionService;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private OscService oscService;

    private Long id = 1L;
    private DatabaseEntity databaseEntity;
    
    @Before
    public void setup() {
        databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        databaseEntity.setConnectionId(id);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertEquals(result.getDatabaseId(), databaseEntity.getDatabaseId());
    }

    @Test
    public void testGetOscDatabaseInfoWhenDatabaseNotPresent() {
        when(databaseRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertEquals(result.getDatabaseId(), null);
    }

    //TODO: You should add similar tests for the other methods. Repeat the process of setting up mock inputs, 
    //calling the method under test, and then using JUnit assertions to verify the outputs.

}