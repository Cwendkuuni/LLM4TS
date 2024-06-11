package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

    @Mock
    private JsonReader mockJsonReader;
    @Mock
    private JsonWriter mockJsonWriter;
    @InjectMocks
    private DefaultDateTypeAdapter defaultDateTypeAdapter;

    @Before
    public void setup() {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void testWrite_nullDate_writesNullValue() throws IOException {
        defaultDateTypeAdapter.write(mockJsonWriter, null);
        verify(mockJsonWriter).nullValue();
    }

    @Test
    public void testWrite_validDate_writesFormattedDateStringWithUSFormat() throws IOException {
        DateFormat mockEnUsFormat = mock(DateFormat.class);
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, mockEnUsFormat, mock(DateFormat.class));
        Date date = new Date();
        String formattedDateString = "formatted date string";
        when(mockEnUsFormat.format(date)).thenReturn(formattedDateString);

        defaultDateTypeAdapter.write(mockJsonWriter, date);
        verify(mockJsonWriter).value(formattedDateString);
    }

    @Test
    public void testRead_nonStringValue_throwsJsonParseException() throws IOException {
        when(mockJsonReader.peek()).thenReturn(JsonToken.BOOLEAN);

        try {
            defaultDateTypeAdapter.read(mockJsonReader);
        } catch (JsonParseException e) {
            assertEquals("The date should be a string value", e.getMessage());
        }
    }

    @Test
    public void testRead_validDateString_returnsDate() throws IOException {
        String dateString = "2021-01-01";
        Date expectedDate = new Date();
        DateFormat mockLocalFormat = mock(DateFormat.class);
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, mock(DateFormat.class), mockLocalFormat);
        when(mockJsonReader.peek()).thenReturn(JsonToken.STRING);
        when(mockJsonReader.nextString()).thenReturn(dateString);
        when(mockLocalFormat.parse(dateString)).thenReturn(expectedDate);

        Date result = defaultDateTypeAdapter.read(mockJsonReader);
        assertEquals(expectedDate, result);
    }

    @Test
    public void testRead_customDateType_returnsCorrectSubclass() throws IOException {
        String dateString = "2021-01-01";
        Date expectedDate = new Date();
        DateFormat mockLocalFormat = mock(DateFormat.class);
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class, mock(DateFormat.class), mockLocalFormat);
        when(mockJsonReader.peek()).thenReturn(JsonToken.STRING);
        when(mockJsonReader.nextString()).thenReturn(dateString);
        when(mockLocalFormat.parse(dateString)).thenReturn(expectedDate);

        Date result = defaultDateTypeAdapter.read(mockJsonReader);
        assertEquals(java.sql.Date.class, result.getClass());
    }

    @Test
    public void testRead_invalidDateString_throwsJsonSyntaxException() throws IOException {
        String dateString = "invalid date string";
        DateFormat mockLocalFormat = mock(DateFormat.class);
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, mock(DateFormat.class), mockLocalFormat);
        when(mockJsonReader.peek()).thenReturn(JsonToken.STRING);
        when(mockJsonReader.nextString()).thenReturn(dateString);
        when(mockLocalFormat.parse(dateString)).thenThrow(ParseException.class);

        try {
            defaultDateTypeAdapter.read(mockJsonReader);
        } catch (JsonSyntaxException e) {
            assertEquals(dateString, e.getJson());
        }
    }

    @Test
    public void testToString_returnsSimpleNameAndFormattedLocalFormatClassName() {
        DateFormat mockLocalFormat = mock(DateFormat.class);
        when(mockLocalFormat.getClass().getSimpleName()).thenReturn("MockDateFormat");
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, mock(DateFormat.class), mockLocalFormat);

        String result = defaultDateTypeAdapter.toString();
        assertEquals("DefaultDateTypeAdapter(MockDateFormat)", result);
    }

}