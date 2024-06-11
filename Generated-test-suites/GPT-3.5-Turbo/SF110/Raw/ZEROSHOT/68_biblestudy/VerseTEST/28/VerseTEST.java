
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class VerseTest {

    private Verse verse;

    @Before
    public void setUp() {
        verse = new Verse();
    }

    @Test
    public void testNew() {
        int id = 1;
        verse.verseCache = new HashMap<>();
        Verse result = verse.New(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(verse.verseCache, times(1)).get(id);
        verify(verse.verseCache, times(1)).put(id, result);
    }

    @Test
    public void testGetId() {
        int translationId = 1;
        int bookId = 2;
        int chapterId = 3;
        int verseId = 4;
        int expectedId = 5;

        verse.translations = new HashMap<>();
        HashMap<Integer, Integer> translation = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Integer>> book = new HashMap<>();
        HashMap<Integer, Integer> chapter = new HashMap<>();
        chapter.put(verseId, expectedId);
        book.put(chapterId, chapter);
        translation.put(bookId, book);
        verse.translations.put(translationId, translation);

        int result = verse.GetId(translationId, bookId, chapterId, verseId);

        assertEquals(expectedId, result);
    }

    @Test
    public void testGet() {
        Translation[] translations = {mock(Translation.class), mock(Translation.class)};
        int bookId = 1;
        int chapterId = 2;
        int verseId = 3;

        ArrayList<Verse> verses = new ArrayList<>();
        verses.add(mock(Verse.class));
        verses.add(mock(Verse.class));

        when(verse.GetId(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(1);

        ArrayList<Verse> result = verse.Get(translations, bookId, chapterId, verseId);

        assertEquals(verses.size(), result.size());
        verify(verse, times(translations.length)).GetId(anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test
    public void testGetWithReferences() {
        Translation[] translations = {mock(Translation.class), mock(Translation.class)};
        Reference begin = mock(Reference.class);
        Reference end = mock(Reference.class);

        ArrayList<Verse> verses = new ArrayList<>();
        verses.add(mock(Verse.class));
        verses.add(mock(Verse.class));

        verse.translations = new HashMap<>();
        verse.translations.put(1, new HashMap<>());

        DbResult result = mock(DbResult.class);
        when(result.next()).thenReturn(true, true, false);
        when(result.getInt("Verse_ID")).thenReturn(1, 2);
        when(Database.Query(anyString())).thenReturn(result);

        ArrayList<Verse> resultVerses = verse.Get(translations, begin, end);

        assertEquals(verses.size(), resultVerses.size());
        verify(Database, times(1)).Query(anyString());
        verify(result, times(2)).next();
        verify(result, times(2)).getInt("Verse_ID");
    }
}
