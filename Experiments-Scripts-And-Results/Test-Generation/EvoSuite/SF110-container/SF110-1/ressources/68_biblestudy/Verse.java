package bible.obj;

import java.util.*;
import java.sql.*;
import bible.util.*;

public class Verse implements Identifible
{
    private int id;
    private String text;
    private TranslationReference transRef;
    private static HashMap translations;
    private static WeakHashMap verseCache;
    private static final String COLUMNS = "Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No";
    
    public static Verse New(final int id) {
        Verse verse = Verse.verseCache.get(new Integer(id));
        if (verse == null) {
            verse = new Verse(id);
        }
        return verse;
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    public String getText() {
        return this.text;
    }
    
    @Override
    public String getValue() {
        return this.getText();
    }
    
    public TranslationReference getTranslationReference() {
        return this.transRef;
    }
    
    public Reference getReference() {
        return this.transRef.getReference();
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + ":[" + this.id + " " + this.transRef + " " + this.text + "]";
    }
    
    public static int GetId(final Translation translation, final Book book, final int chapterId, final int verseId) {
        return GetId(translation.getId(), book.getId(), chapterId, verseId);
    }
    
    public static ArrayList Get(final Translation[] translations, final int bookId, final int chapterId, final int verseId) {
        final ArrayList verses = new ArrayList(translations.length);
        for (int index = 0; index < translations.length; ++index) {
            verses.add(New(GetId(translations[index].getId(), bookId, chapterId, verseId)));
        }
        return verses;
    }
    
    public static ArrayList Get(final Translation[] translations, final Reference begin, final Reference end) {
        System.out.println("Verse.Get translations=" + Util.ToString(translations));
        final ArrayList verses = new ArrayList();
        DbResult result = null;
        final String query = "select Verse_ID from Verse where " + ((translations != null && translations.length > 0) ? ("Translation_ID in (" + Util.ToString(Util.ToIntArray(translations)) + ") and ") : "") + "Book_ID >= " + begin.getBookId() + " and Book_ID <= " + end.getBookId() + " and Chapter >= " + begin.getChapterNo() + " and Chapter <= " + end.getChapterNo() + " and Verse_No >= " + begin.getVerseNo() + " and Verse_No <= " + end.getVerseNo();
        System.out.println("Verse.Get query=" + query);
        try {
            result = Database.Query(query);
            while (result.next()) {
                verses.add(New(result.getInt("Verse_ID")));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbResult.Close(result);
        }
        return verses;
    }
    
    public static int GetId(int translationId, final int bookId, final int chapterId, final int verseId) {
        translationId = Translation.Normalize(translationId);
        HashMap translation = Verse.translations.get(new Integer(translationId));
        if (translation == null) {
            translation = new HashMap(11);
            Verse.translations.put(new Integer(translationId), translation);
        }
        HashMap book = translation.get(new Integer(bookId));
        if (book == null) {
            book = new HashMap(97);
            translation.put(new Integer(bookId), book);
        }
        HashMap chapter = book.get(new Integer(chapterId));
        if (chapter == null) {
            chapter = new HashMap(47);
            book.put(new Integer(chapterId), chapter);
        }
        final Integer id = chapter.get(new Integer(verseId));
        int intId;
        if (id == null) {
            intId = ReadId(translationId, bookId, chapterId, verseId);
            if (intId > 0) {
                chapter.put(new Integer(verseId), new Integer(intId));
            }
        }
        else {
            intId = id;
        }
        System.out.println("Verse.GetId(" + translationId + ", " + bookId + ", " + chapterId + ", " + verseId + ")=" + intId);
        return intId;
    }
    
    private Verse(final int id) {
        DbResult result = null;
        final String query = "select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = " + id;
        try {
            result = Database.Query(query);
            if (result.next()) {
                this.complete(result);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbResult.Close(result);
        }
    }
    
    private Verse(final DbResult result) throws SQLException {
        this.complete(result);
    }
    
    private void complete(final DbResult result) throws SQLException {
        this.id = result.getInt("Verse_ID");
        this.text = result.getString("Text");
        this.transRef = new TranslationReference(Translation.Get(result.getInt("Translation_ID")), new Reference(result.getInt("Book_ID"), result.getInt("Chapter"), result.getInt("Verse_No")));
        Verse.verseCache.put(new Integer(this.id), this);
    }
    
    private static int ReadId(final int translationId, final int bookId, final int chapterId, final int verseId) {
        int id = -1;
        DbResult result = null;
        final String query = "select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Translation_ID = " + translationId + " and Book_ID = " + bookId + " and Chapter = " + chapterId + " and Verse_No = " + verseId;
        try {
            result = Database.Query(query);
            if (result.next()) {
                id = new Verse(result).getId();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbResult.Close(result);
        }
        return id;
    }
    
    static {
        Verse.translations = new HashMap();
        Verse.verseCache = new WeakHashMap();
    }
}
