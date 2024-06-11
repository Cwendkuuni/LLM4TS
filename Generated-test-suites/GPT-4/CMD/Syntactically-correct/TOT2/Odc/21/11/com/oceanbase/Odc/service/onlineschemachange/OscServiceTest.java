package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OscServiceTest {

    @InjectMocks
    OscService oscService;

    @Mock
    DatabaseRepository databaseRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Normal scenario
    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 9L;
        DatabaseEntity entity = new DatabaseEntity();
        entity.setDatabaseId(1L);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(entity));
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

        assertNotNull(oscDatabase);
        assertEquals(entity.getDatabaseId(), oscDatabase.getDatabaseId());
    }

    // Edge case: record not found
    @Test
    public void testGetOscDatabaseInfo_NotFound() {
        Long id = 10L;

        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

        assertNotNull(oscDatabase);
        assertNull(oscDatabase.getDatabaseId());
    }

    // Exception handling scenario to be written when error situations are clear
    
}