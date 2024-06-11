import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

    private AddressToUsAddressFunctor addressToUsAddressFunctor;

    @Before
    public void setUp() {
        addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
    }

    @Test
    public void testExecute_BasicCase() {
        GeoAddress geoAddress = new GeoAddress();
    
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
    
        CountryNameCode countryNameCode = new CountryNameCode();
        countryNameCode.setContent("US");
        country.getCountryNameCode().add(countryNameCode);
    
        AdministrativeArea administrativeArea = new AdministrativeArea();
        AdministrativeAreaName administrativeAreaName = new AdministrativeAreaName();
        administrativeAreaName.setContent("California");
        administrativeArea.getAdministrativeAreaName().add(administrativeAreaName);
    
        Locality locality = new Locality();
        LocalityName localityName = new LocalityName();
        localityName.setContent("Los Angeles");
        locality.getLocalityName().add(localityName);
    
        PostalCode postalCode = new PostalCode();
        PostalCodeNumber postalCodeNumber = new PostalCodeNumber();
        postalCodeNumber.setContent("90001");
        postalCode.getPostalCodeNumber().add(postalCodeNumber);
        locality.setPostalCode(postalCode);
    
        Thoroughfare thoroughfare = new Thoroughfare();
        ThoroughfareNameType thoroughfareNameType = new ThoroughfareNameType();
        thoroughfareNameType.setContent("Main Street");
        thoroughfare.getThoroughfareName().add(thoroughfareNameType);
        locality.setThoroughfare(thoroughfare);
    
        administrativeArea.setLocality(locality);
        country.setAdministrativeArea(administrativeArea);
    
        addressDetails.setCountry(country);
        geoAddress.setAddressDetails(addressDetails);
    
        GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);
    
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCity());
        assertEquals("90001", result.getPostalCode());
        assertEquals("Main Street", result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCounty());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testExecute_NullAddress() {
        GeoUsAddress result = addressToUsAddressFunctor.execute(null);

        assertNull(result);
    }

    @Test
    public void testExecute_NullCountry() {
        GeoAddress geoAddress = new GeoAddress();

        AddressDetails addressDetails = new AddressDetails();
        geoAddress.setAddressDetails(addressDetails);

        GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCounty());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testExecute_EmptyCountryNames() {
        GeoAddress geoAddress = new GeoAddress();

        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();

        country.getCountryNameCode().add(new CountryNameCode());

        addressDetails.setCountry(country);
        geoAddress.setAddressDetails(addressDetails);

        GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCounty());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testExecute_NullAdministrativeArea() {
        GeoAddress geoAddress = new GeoAddress();

        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.getCountryNameCode().add(new CountryNameCode());
        addressDetails.setCountry(country);
        geoAddress.setAddressDetails(addressDetails);

        GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

        assertNull(result.getState());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCounty());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testExecute_NullLocality() {
        GeoAddress geoAddress = new GeoAddress();

        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.getCountryNameCode().add(new CountryNameCode());
        AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.getAdministrativeAreaName().add(new AdministrativeAreaName());
        country.setAdministrativeArea(administrativeArea);
        addressDetails.setCountry(country);
        geoAddress.setAddressDetails(addressDetails);

        GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCounty());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testExecute_NullPostalCode() {
        GeoAddress geoAddress = new GeoAddress();

        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.getCountryNameCode().add(new CountryNameCode());
        AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.getAdministrativeAreaName().add(new AdministrativeAreaName());
        Locality locality = new Locality();
        locality.getLocalityName().add(new LocalityName());
        administrativeArea.setLocality(locality);
        country.setAdministrativeArea(administrativeArea);
        addressDetails.setCountry(country);
        geoAddress.setAddressDetails(addressDetails);

        GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCounty());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testExecute_NoThoroughfare() {
        GeoAddress geoAddress = new GeoAddress();

        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.getCountryNameCode().add(new CountryNameCode());
        AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.getAdministrativeAreaName().add(new AdministrativeAreaName());
        Locality locality = new Locality();
        locality.getLocalityName().add(new LocalityName());
        PostalCode postalCode = new PostalCode();
        postalCode.getPostalCodeNumber().add(new PostalCodeNumber());
        locality.setPostalCode(postalCode);
        administrativeArea.setLocality(locality);
        country.setAdministrativeArea(administrativeArea);
        addressDetails.setCountry(country);
        geoAddress.setAddressDetails(addressDetails);

        GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCounty());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testExecute_NullCoordinate() {
        GeoAddress geoAddress = new GeoAddress();

        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.getCountryNameCode().add(new CountryNameCode());
        AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.getAdministrativeAreaName().add(new AdministrativeAreaName());
        Locality locality = new Locality();
        locality.getLocalityName().add(new LocalityName());
        PostalCode postalCode = new PostalCode();
        postalCode.getPostalCodeNumber().add(new PostalCodeNumber());
        Thoroughfare thoroughfare = new Thoroughfare();
        thoroughfare.getThoroughfareName().add(new ThoroughfareNameType());
        locality.setThoroughfare(thoroughfare);
        locality.setPostalCode(postalCode);
        administrativeArea.setLocality(locality);
        country.setAdministrativeArea(administrativeArea);
        addressDetails.setCountry(country);
        geoAddress.setAddressDetails(addressDetails);

        GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

}