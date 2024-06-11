package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OscServiceTest {

    @InjectMocks
    OscService oscService;
    
    @Mock
    DatabaseRepository databaseRepository;

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    ConnectionService connectionService;

    @Mock
    ScheduleRepository scheduleRepository;

    @Test
    public void testGetOscDatabaseInfoExists() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertEquals(result.getDatabaseId(), databaseEntity.getDatabaseId());
    }
    
    @Test
    public void testGetOscDatabaseInfoNotExists() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertEquals(result.getDatabaseId(), null);
    }
    
    //Further test can be added to test edge cases, exceptions, etc
    // Do not forget to test for swapTable() method analogous to getOscDatabaseInfo() as well.     
}