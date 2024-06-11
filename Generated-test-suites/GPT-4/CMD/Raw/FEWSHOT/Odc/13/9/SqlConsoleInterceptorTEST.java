/*
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor interceptor;

    @Mock
    private ConnectionSession mockSession;
    @Mock
    private SqlAsyncExecuteReq mockRequest;
    @Mock
    private SqlAsyncExecuteResp mockResponse;
    @Mock
    private Map<String, Object> mockContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockContext = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        // Set up
        this.mockContext.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, Boolean.TRUE);

        // Execute
        boolean result = interceptor.doPreHandle(mockRequest, mockResponse, mockSession, mockContext);

        // Validate
        assertTrue(result);

        // Verify
        verify(mockSession, times(0)).finish(SqlExecuteStatus.ERROR, (String) null);
    }

    @Test
    public void testDoAfterCompletion() {
        // Set up
        mockResponse.setSqls(new ArrayList<>());

        // Execute
        interceptor.doAfterCompletion(mockResponse, mockSession, mockContext);
        
        // Verify
        verify(mockResponse, atLeastOnce()).getStatus();
    }

}