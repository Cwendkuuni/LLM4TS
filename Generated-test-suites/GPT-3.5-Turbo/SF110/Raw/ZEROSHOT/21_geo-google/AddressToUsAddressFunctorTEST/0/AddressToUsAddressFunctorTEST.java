package geo.google.mapping;

import geo.google.datamodel.*;
import geo.google.*;
import org.junit.*;
import static org.junit.Assert.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {

    private AddressToUsAddressFunctor addressToUsAddressFunctor;

    @Before
    public void setUp() {
        addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
    }

    @Test
    public void testExecute() {
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("USA");
        country.getCountryNameCode().add(countryNameCode);
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("San Francisco");
        locality.getLocalityName().add(localityName);
        PostalCode postalCode = new PostalCode();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("94101");
        postalCode.getPostalCodeNumber().add(postalCodeNumber);
        locality.setPostalCode(postalCode);
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);

        GeoUsAddress ret = addressToUsAddressFunctor.execute(addr);

        assertEquals("USA", ret.getCountry());
        assertEquals("California", ret.getState());
        assertEquals("San Francisco", ret.getCity());
        assertEquals("94101", ret.getPostalCode());
    }

    @Test
    public void testExecute_withNullCountry() {
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());

        GeoUsAddress ret = addressToUsAddressFunctor.execute(addr);

        assertNull(ret.getCountry());
        assertNull(ret.getState());
        assertNull(ret.getCity());
        assertNull(ret.getPostalCode());
    }

    @Test
    public void testExecute_withNullAdministrativeArea() {
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("USA");
        country.getCountryNameCode().add(countryNameCode);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);

        GeoUsAddress ret = addressToUsAddressFunctor.execute(addr);

        assertEquals("USA", ret.getCountry());
        assertNull(ret.getState());
        assertNull(ret.getCity());
        assertNull(ret.getPostalCode());
    }

    @Test
    public void testExecute_withNullLocality() {
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("USA");
        country.getCountryNameCode().add(countryNameCode);
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        country.setAdministrativeArea(adminArea);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);

        GeoUsAddress ret = addressToUsAddressFunctor.execute(addr);

        assertEquals("USA", ret.getCountry());
        assertEquals("California", ret.getState());
        assertNull(ret.getCity());
        assertNull(ret.getPostalCode());
    }

    @Test
    public void testExecute_withNullPostalCode() {
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("USA");
        country.getCountryNameCode().add(countryNameCode);
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("San Francisco");
        locality.getLocalityName().add(localityName);
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);

        GeoUsAddress ret = addressToUsAddressFunctor.execute(addr);

        assertEquals("USA", ret.getCountry());
        assertEquals("California", ret.getState());
        assertEquals("San Francisco", ret.getCity());
        assertNull(ret.getPostalCode());
    }

    @Test
    public void testExecute_withNullThoroughfare() {
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("USA");
        country.getCountryNameCode().add(countryNameCode);
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("San Francisco");
        locality.getLocalityName().add(localityName);
        PostalCode postalCode = new PostalCode();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("94101");
        postalCode.getPostalCodeNumber().add(postalCodeNumber);
        locality.setPostalCode(postalCode);
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);

        GeoUsAddress ret = addressToUsAddressFunctor.execute(addr);

        assertEquals("USA", ret.getCountry());
        assertEquals("California", ret.getState());
        assertEquals("San Francisco", ret.getCity());
        assertEquals("94101", ret.getPostalCode());
        assertNull(ret.getAddressLine1());
        assertNull(ret.getAddressLine2());
    }

    @Test
    public void testExecute_withMultipleThoroughfares() {
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("USA");
        country.getCountryNameCode().add(countryNameCode);
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("San Francisco");
        locality.getLocalityName().add(localityName);
        PostalCode postalCode = new PostalCode();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("94101");
        postalCode.getPostalCodeNumber().add(postalCodeNumber);
        locality.setPostalCode(postalCode);
        adminArea.setLocality(locality);
        Thoroughfare thoroughfare = new Thoroughfare();
        ThoroughfareNameType thoroughfareName1 = new ThoroughfareNameType();
        thoroughfareName1.setContent("Main St");
        thoroughfare.getThoroughfareName().add(thoroughfareName1);
        ThoroughfareNameType thoroughfareName2 = new ThoroughfareNameType();
        thoroughfareName2.setContent("Second St");
        thoroughfare.getThoroughfareName().add(thoroughfareName2);
        locality.setThoroughfare(thoroughfare);
        country.setAdministrativeArea(adminArea);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);

        GeoUsAddress ret = addressToUsAddressFunctor.execute(addr);

        assertEquals("USA", ret.getCountry());
        assertEquals("California", ret.getState());
        assertEquals("San Francisco", ret.getCity());
        assertEquals("94101", ret.getPostalCode());
        assertEquals("Main St", ret.getAddressLine1());
        assertEquals("Second St", ret.getAddressLine2());
    }

    @Test
    public void testExecute_withCoordinate() {
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("USA");
        country.getCountryNameCode().add(countryNameCode);
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("San Francisco");
        locality.getLocalityName().add(localityName);
        PostalCode postalCode = new PostalCode();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("94101");
        postalCode.getPostalCodeNumber().add(postalCodeNumber);
        locality.setPostalCode(postalCode);
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);
        addr.setCoordinate(new Coordinate());

        GeoUsAddress ret = addressToUsAddressFunctor.execute(addr);

        assertEquals("USA", ret.getCountry());
        assertEquals("California", ret.getState());
        assertEquals("San Francisco", ret.getCity());
        assertEquals("94101", ret.getPostalCode());
        assertNotNull(ret.getCoordinate());
        assertNotSame(addr.getCoordinate(), ret.getCoordinate());
        assertEquals(addr.getCoordinate().getX(), ret.getCoordinate().getX(), 0);
        assertEquals(addr.getCoordinate().getY(), ret.getCoordinate().getY(), 0);
    }

    @Test
    public void testExecute_withNullAddressDetails() {
        GeoAddress addr = new GeoAddress();

        GeoUsAddress ret = addressToUsAddressFunctor.execute(addr);

        assertNull(ret.getCountry());
        assertNull(ret.getState());
        assertNull(ret.getCity());
        assertNull(ret.getPostalCode());
    }

}